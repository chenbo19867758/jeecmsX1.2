package com.jeecms.util;

import org.apache.commons.lang3.StringUtils;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * @author Zhu Kaixiao
 * @version 1.0
 * @date 2019/8/6 11:18
 * @copyright 江西金磊科技发展有限公司 All rights reserved. Notice
 * 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */
public class JdbcUtil {

    /**
     * 解析jdbcUrl, 提取url中的host, port, databaseName
     *
     * @param jdbcUrl jdbcUrl
     * @return java.util.Map<java.lang.String, java.lang.Object> keys: host, port, databaseName
     * @author Zhu Kaixiao
     * @date 2019/8/6 12:04
     **/
    public static Map<String, Object> resolveJdbcUrl(String jdbcUrl) {
        jdbcUrl = jdbcUrl.toLowerCase();
        if (jdbcUrl.startsWith("jdbc:mysql")) {
            return resolveMysqlUrl(jdbcUrl);
        } else if (jdbcUrl.startsWith("jdbc:sqlserver")) {
            return resolveSqlServerUrl(jdbcUrl);
        } else if (jdbcUrl.startsWith("jdbc:oracle")) {
            return resolveOracleUrl(jdbcUrl);
        }
        return Collections.emptyMap();
    }

    private static Map<String, Object> resolveMysqlUrl(String jdbcUrl) {
        // jdbc:mysql://localhost:3306/test?useUnicode=true&characterEncoding=GBK
        String tmp = jdbcUrl.split("://")[1];
        Integer port = null;
        String host = StringUtils.substringBefore(tmp, "/");
        if (host.contains(":")) {
            String[] sp = host.split(":");
            host = sp[0];
            try {
                port = Integer.parseInt(sp[1]);
            } catch (Exception ignore) {

            }
        }

        String dbName = StringUtils.substringBefore(StringUtils.substringAfter(tmp, "/"), "?");

        Map<String, Object> rMap = new HashMap<>(3);
        rMap.put("databaseName", dbName);
        rMap.put("host", host);
        rMap.put("port", Optional.ofNullable(port).orElse(3306));
        return rMap;
    }

    private static Map<String, Object> resolveSqlServerUrl(String jdbcUrl) {
        // jdbc:sqlserver://192.168.1.130:1433;database=ahos;user=sa;password=ahtec";
        String tmp = jdbcUrl.split("://")[1];
        Integer port = null;
        String host = StringUtils.substringBefore(tmp, ";");
        if (host.contains(":")) {
            String[] sp = host.split(":");
            host = sp[0];
            try {
                port = Integer.parseInt(sp[1]);
            } catch (Exception ignore) {

            }
        }

        String dbName = null;
        for (String s : tmp.split(";")) {
            s = s.toLowerCase();
            if (s.startsWith("database=") || s.startsWith("databasename=")) {
                dbName = s.split("=")[1];
            }
        }

        Map<String, Object> rMap = new HashMap<>(3);
        rMap.put("databaseName", dbName);
        rMap.put("host", host);
        rMap.put("port", Optional.ofNullable(port).orElse(1433));
        return rMap;
    }

    private static Map<String, Object> resolveOracleUrl(String jdbcUrl) {
        // jdbc:oracle:thin:@192.168.1.250:1521:devdb
        String tmp = jdbcUrl.split("@")[1];
        Integer port = null;
        String dbName;
        String host = StringUtils.substringBefore(tmp, ":");
        tmp = StringUtils.substringAfter(tmp, ":");
        if (tmp.contains(":") || tmp.contains("/")) {
            String[] sp = tmp.split("[:/]");
            port = Integer.valueOf(sp[0]);
            dbName = sp[1];
        } else {
            dbName = tmp;
        }
        dbName = dbName.split("\\?")[0];
        Map<String, Object> rMap = new HashMap<>(3);
        rMap.put("databaseName", dbName);
        rMap.put("host", host);
        rMap.put("port", Optional.ofNullable(port).orElse(1521));
        return rMap;
    }
}
