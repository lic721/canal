package com.alibaba.otter.canal.admin.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * @author zhihua.li
 */
@Component
public class ScheduledTask {

    private static final Logger logger = LoggerFactory.getLogger(ScheduledTask.class);
    private int                 i;

    @Scheduled(cron = "*/15 * * * * ?")
    public void execute() {
        logger.info("thread id:{}, execute times:{}", Thread.currentThread().getId(), ++i);
    }

}
