package com.alibaba.otter.canal.admin.connector;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.function.Function;

import com.alibaba.otter.canal.admin.model.DbInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.env.Environment;

import com.alibaba.otter.canal.admin.config.SpringContext;

import static java.util.jar.Pack200.Packer.PASS;

/**
 * @author zhihua.li
 */
public class MySqlConnectors {

    private static final Logger logger = LoggerFactory.getLogger(MySqlConnectors.class);

    public static boolean execute(DbInfo di) {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            StringBuilder url = new StringBuilder();
            // 格式:"jdbc:mysql://10.181.24.56:3306/saasdemo?useSSL=false&connectTimeout=3000"
            url.append("jdbc:mysql://")
                .append(di.getDbIp())
                .append(":")
                .append(di.getDbPort())
                .append("/")
                .append(di.getDbName())
                .append("?useSSL=false&connectTimeout=3000");
            Connection connection = DriverManager.getConnection(url.toString(), di.getDbUserName(), di.getDbPassword());
            connection.close();
        } catch (Exception e) {
            logger.error("connect database, failed", e);
            return false;
        }

        return true;
    }
}
