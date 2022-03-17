package com.alibaba.otter.canal.admin.connector;

import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.otter.canal.admin.common.JdbcUtil;
import com.alibaba.otter.canal.admin.model.DbInfo;

/**
 * @author zhihua.li
 */
public class MySqlConnectors {

    private static final Logger logger = LoggerFactory.getLogger(MySqlConnectors.class);

    public static boolean execute(DbInfo dbInfo) {
        try {
            JdbcUtil jdbcUtil = connectByDbInfo(dbInfo);
            jdbcUtil.release();
        } catch (Exception e) {
            logger.error("connect database, failed", e);
            return false;
        }

        return true;
    }

    public static JdbcUtil connectByDbInfo(DbInfo dbInfo) throws SQLException {
        StringBuilder url = new StringBuilder();
        // 格式:"jdbc:mysql://10.181.24.56:3306/saasdemo?useSSL=false&connectTimeout=3000"
        url.append("jdbc:mysql://")
            .append(dbInfo.getDbIp())
            .append(":")
            .append(dbInfo.getDbPort())
            .append("/")
            .append(dbInfo.getDbName())
            .append("?useSSL=false&connectTimeout=3000");
        return new JdbcUtil("com.mysql.jdbc.Driver", url.toString(), dbInfo.getDbUserName(), dbInfo.getDbPassword());
    }
}
