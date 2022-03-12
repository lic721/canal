package com.alibaba.otter.canal.admin.service.impl;

import java.util.List;

import com.alibaba.fastjson.JSON;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.alibaba.otter.canal.admin.model.DbInfo;
import com.alibaba.otter.canal.admin.model.DbTransferConfig;
import com.alibaba.otter.canal.admin.model.DbTransferHistory;

/**
 * @author zhihua.li
 */
@Component
public class ScheduledTask {

    private static final Logger logger = LoggerFactory.getLogger(ScheduledTask.class);

    @Scheduled(cron = "*/15 * * * * ?")
    public void execute() {
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
            int runningCnt = DbTransferHistory.find.query()
                .where()
                .eq("sourceDbInfoId", dbTransferConfig.getDbInfoId())
                .isNull("end_time")
                .findCount();
            if (runningCnt > 0) {
                logger.info("存在运行中的扩容, 数据库信息ID:{}, 数目:{}", dbTransferConfig.getDbInfoId(), runningCnt);
                // do nothing
                continue;
            }

            // 无进行中的迁移
            // 判断是否满足迁移条件
//            dbTransferConfig.getDbInfoId()

            // 触发迁移
            // 新建canal_instance_config
            // 从canal_cluster和canal_config中获取mq配置, 启动mq消费

        }

    }

}
