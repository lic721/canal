package com.alibaba.otter.canal.admin.service.impl;

import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.alibaba.otter.canal.admin.service.CanalInstanceService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.alibaba.otter.canal.admin.common.JdbcUtil;
import com.alibaba.otter.canal.admin.connector.MySqlConnectors;
import com.alibaba.otter.canal.admin.dto.DbTransferPlanDTO;
import com.alibaba.otter.canal.admin.model.*;

/**
 * @author zhihua.li
 */
@Component
public class ScheduledTask {

    private static final Logger logger             = LoggerFactory.getLogger(ScheduledTask.class);

    private static final Date   DEFAULT_START_TIME = new Date(0L);

    @Autowired
    CanalInstanceService        canalInstanceConfigService;

    @Scheduled(cron = "*/15 * * * * ?")
    public void execute() {

        int runningCnt = DbTransferHistory.find.query().where().isNull("endTime").findCount();
        if (runningCnt > 0) {
            logger.info("存在运行中的扩容, 数目:{}", runningCnt);
            return;
        }

        List<CanalCluster> canalClusters = CanalCluster.find.query().findList();
        if (canalClusters.size() != 1) {
            logger.warn("canal集群必须配置成1个, 当前数目:{}", canalClusters.size());
            return;
        }
        Long clusterId = canalClusters.get(0).getId();

        List<DbInfo> sortedDbInfoList = DbInfo.find.query().order().asc("sequence").findList();

        List<DbTransferConfig> dbTransferConfigList = DbTransferConfig.find.query().findList();
        logger.info("数据库扩容配置表 size:{}", dbTransferConfigList.size());
        for (DbTransferConfig dbTransferConfig : dbTransferConfigList) {
            logger.info("数据库扩容配置:{}", JSON.toJSONString(dbTransferConfig));
            // 如果有进行中的迁移, do nothing
            DbInfo dbInfo = DbInfo.find.byId(dbTransferConfig.getDbInfoId());
            if (dbInfo == null) {
                logger.info("数据库信息ID不存在:{}", dbTransferConfig.getDbInfoId());
                continue;
            }

            JdbcUtil jdbcUtil = getJdbcUtil(dbInfo);
            if (jdbcUtil == null) {
                continue;
            }
            try {
                // 判断是否满足迁移条件
                // 关键表的总条数
                long keyTableTotalCount = getKeyTableTotalCount(dbTransferConfig, jdbcUtil);
                if (keyTableTotalCount > dbTransferConfig.getTableCountThreshold()) {
                    // 触发迁移
                    // 插入扩容记录,通过唯一索引来防止重复扩容
                    DbTransferHistory tempDbTransferHistory = insertDbTransferHistory(dbTransferConfig);

                    // 计算最优迁移方案:优先迁移到顺序小的db_info,迁移后剩余数据量小于阈值
                    DbTransferPlanDTO transferPlanDTO = new DbTransferPlanDTO();
                    transferPlanDTO.setSourceDbInfoId(tempDbTransferHistory.getSourceDbInfoId());

                    String sql = "select count(*) as cnt," + dbTransferConfig.getTenantCodeColumnName()
                                 + " as tenantCode from " + dbTransferConfig.getKeyTableName() + " group by "
                                 + dbTransferConfig.getTenantCodeColumnName() + " order by cnt desc";
                    try {
                        List<String> transferTenantCodeList = new ArrayList<>();
                        List<Map> queryTableList = jdbcUtil.selectByParams(sql, null);
                        long sum = 0L;
                        for (Map map : queryTableList) {
                            long count = Long.parseLong(map.get("cnt").toString());
                            String tenantCode = map.get("tenantCode").toString();
                            logger.info("表:{}, 租户编码:{}, 该租户数据量:{}",
                                dbTransferConfig.getKeyTableName(),
                                tenantCode,
                                count);

                            transferTenantCodeList.add(tenantCode);
                            sum += count;
                            long remainCount = keyTableTotalCount - sum;
                            // 剩余数据量小于阈值, 方案完成
                            if (remainCount < dbTransferConfig.getTableCountThreshold()) {
                                logger.info("预估迁移后剩余数据量:{}, 阈值:{}",
                                    remainCount,
                                    dbTransferConfig.getTableCountThreshold());
                            }
                        }
                        transferPlanDTO.setTransferTenantCodeList(transferTenantCodeList);
                        // 根据DbInfo的顺序来决定targetDbInfo
                        for (DbInfo targetDbInfo : sortedDbInfoList) {
                            if (!targetDbInfo.getDbIp().equals(dbInfo.getDbIp())) {
                                transferPlanDTO.setTargetDbInfoId(targetDbInfo.getId());
                                break;
                            }
                        }
                        // mq
                        transferPlanDTO.setMqTopic("source" + transferPlanDTO.getSourceDbInfoId());

                    } catch (SQLException e) {
                        logger.error("sql:" + sql, e);
                        continue;
                    }

                    // 新建canal_instance_config
                    CanalInstanceConfig canalInstanceConfig = new CanalInstanceConfig();
                    canalInstanceConfig
                        .setName(transferPlanDTO.getSourceDbInfoId() + "_" + transferPlanDTO.getTargetDbInfoId());
                    canalInstanceConfig.setClusterServerId("cluster:" + clusterId);
                    // todo content
                    canalInstanceConfig.setContent("");
                    canalInstanceConfigService.save(canalInstanceConfig);

                    // 重新启动mq消费

                    // 启动canalInstance
                    canalInstanceConfigService.remoteOperation(canalInstanceConfig.getId(), null, "start");

                    // 更新扩容历史
                    transferPlanDTO.setStartTime(new Date());
                    DbTransferHistory transferHistory = DbTransferHistory.find.query()
                        .where()
                        .eq("sourceDbInfoId", transferPlanDTO.getSourceDbInfoId())
                        .eq("startTime", DEFAULT_START_TIME)
                        .findOne();
                    transferHistory.setStartTime(transferPlanDTO.getStartTime());
                    transferHistory.setTargetDbInfoId(transferPlanDTO.getTargetDbInfoId());
                    transferHistory.setDbTransferConfigJson(JSON.toJSONString(dbTransferConfig));
                    transferHistory.setRemark("");
                    transferHistory.setCanalInstanceConfigId(transferPlanDTO.getCanalInstanceConfigId());
                    transferHistory.setMqTopic(transferPlanDTO.getMqTopic());
                    transferHistory.update();
                }
            } finally {
                jdbcUtil.release();
            }
        }

    }

    private DbTransferHistory insertDbTransferHistory(DbTransferConfig dbTransferConfig) {
        DbTransferHistory dbTransferHistory = new DbTransferHistory();
        dbTransferHistory.setStartTime(DEFAULT_START_TIME);
        dbTransferHistory.setSourceDbInfoId(dbTransferConfig.getDbInfoId());
        dbTransferHistory.setTargetDbInfoId(-1L);
        dbTransferHistory.setCanalInstanceConfigId(-1L);
        dbTransferHistory.setMqTopic("");
        dbTransferHistory.insert();
        return dbTransferHistory;
    }

    private JdbcUtil getJdbcUtil(DbInfo dbInfo) {
        try {
            return MySqlConnectors.connectByDbInfo(dbInfo);
        } catch (SQLException e) {
            logger.error("连接失败: " + JSON.toJSONString(dbInfo), e);
            return null;
        }
    }

    private long getKeyTableTotalCount(DbTransferConfig dbTransferConfig, JdbcUtil jdbcUtil) {
        if (dbTransferConfig.getTableCountThreshold() <= 0) {
            return 0L;
        }

        String sql = "select count(*) as cnt from " + dbTransferConfig.getKeyTableName();
        try {
            List<Map> queryTableList = jdbcUtil.selectByParams(sql, null);
            return Long.parseLong(queryTableList.get(0).get("cnt").toString());
        } catch (SQLException e) {
            logger.error("sql:" + sql, e);
            return 0L;
        }
    }

}
