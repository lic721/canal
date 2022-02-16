package com.alibaba.otter.canal.admin.service.impl;

import java.util.List;

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
            // 如果有进行中的迁移, do nothing
            DbInfo dbInfo = DbInfo.find.byId(dbTransferConfig.getDbInfoId());
            if (dbInfo == null) {
                continue;
            }
            int runningCnt = DbTransferHistory.find.query()
                .where()
                .eq("sourceDbInfoId", dbTransferConfig.getDbInfoId())
                .isNull("end_time")
                .findCount();
            if (runningCnt > 0) {
                // do nothing
                continue;
            }

            // 如果无进行中的迁移, 判断是否满足迁移条件
//            if(){
//
//            }


        }

    }

}
