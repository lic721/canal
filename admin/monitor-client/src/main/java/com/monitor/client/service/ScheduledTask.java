package com.monitor.client.service;

import com.monitor.client.common.NetworkUtil;
import com.monitor.client.zk.DistributedSystemClient;
import com.monitor.client.zk.DistributedSystemServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * @author zhihua.li
 */
@Component
public class ScheduledTask implements InitializingBean {

    private static final Logger logger = LoggerFactory.getLogger(ScheduledTask.class);

    @Autowired
    DistributedSystemServer server;

    @Autowired
    DistributedSystemClient client;

    /**
     * 每5s上传负载信息
     */
    @Scheduled(cron = "*/5 * * * * ?")
    public void execute() {

        // 获取zookeeper中的服务器节点
    }

    @Override
    public void afterPropertiesSet() throws Exception {

        // ip注册到zookeeper

        // 获取与zookeeper通信的客户端连接
        server.getZkClient();
        // 一启动就去zookeeper上注册服务器信息
        server.connectZK(NetworkUtil.getLocalIP());
    }
}
