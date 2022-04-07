package com.monitor.client.zk;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.ZooDefs.Ids;
import org.apache.zookeeper.ZooKeeper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class DistributedSystemServer {

    @Value("${zkServers}")
    private String    zkhosts;

    private ZooKeeper zk = null;

    public void getZkClient() throws Exception {

        // 服务器在需求中并不需要做任何监听
        zk = new ZooKeeper(zkhosts, GlobalConstants.sessionTimeout, null);
    }

    /**
     * 向zookeeper中的/servers下创建子节点
     * 
     * @throws InterruptedException
     * @throws KeeperException
     */
    public void connectZK(String serverName) throws Exception {

        // 先创建出父节点
        if (zk.exists(GlobalConstants.parentZnodePath, false) == null) {
            zk.create(GlobalConstants.parentZnodePath, null, Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
        }

        // 连接zk创建znode
        zk.create(GlobalConstants.parentZnodePath + "/",
            (serverName).getBytes(),
            Ids.OPEN_ACL_UNSAFE,
            CreateMode.EPHEMERAL_SEQUENTIAL);
        System.out.println("server " + serverName + " is online ......");

    }

    // 服务器的具体业务处理功能
    private void handle(String serverName) throws Exception {
        System.out.println("server " + serverName + " is waiting for task process......");
        Thread.sleep(Long.MAX_VALUE);

    }

}
