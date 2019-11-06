package com.example.springbootutils.utils.dbpool;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 数据库连接查询
 */
public class DatabaseConnTool {
    private static final Log LOGGER = LogFactory.getLog(DatabaseConnTool.class);

    /**
     * 执行SQL
     */
    public static void executeSql(String sql, DBInfo dbInfo) {
        Connection conn = null;
        try {
            LOGGER.info("sql语句 ： " + sql);
            LOGGER.info("dbUrl:" + dbInfo.getDbUrl() + "?username:" + dbInfo.getDbUsername() + "&password:" +
                    dbInfo.getDbPassword());
            conn = ConnectionPoolUtils.getConnection(dbInfo);
            Statement st = conn.createStatement();
            // 执行SQL语句
            st.execute(sql);
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            throw new RuntimeException("SQL执行失败");
        } finally {
            ConnectionPoolUtils.returnConnection(dbInfo, conn);
        }
    }

    /**
     * 执行查询SQL
     */
    public static List<Map<String, Object>> querySql(String sql, DBInfo dbInfo) {
        Connection conn = null;
        try {
            LOGGER.info("查询语句 ： " + sql);
            LOGGER.info("dbUrl:" + dbInfo.getDbUrl() + "?username:" + dbInfo.getDbUsername() + "&password:" + dbInfo.getDbPassword());
            conn = ConnectionPoolUtils.getConnection(dbInfo);
            long t1 = System.currentTimeMillis();
            List<Map<String, Object>> list = querySql(sql, conn);
            LOGGER.info("查询时间耗时： " + (System.currentTimeMillis() - t1) + "ms");
            LOGGER.info("查寻结果条数： " + list.size());
            return list;
        } catch (Exception e) {
            e.printStackTrace();
            LOGGER.error(e.getMessage());
            throw new RuntimeException("数据库查询失败");
        } finally {
            ConnectionPoolUtils.returnConnection(dbInfo, conn);
        }
    }

    /**
     * 查询数据库，返回多条记录
     */
    private static List<Map<String, Object>> querySql(String sql, Connection conn) {
        try {
            List<Map<String, Object>> list = new ArrayList<>();
            Statement st = conn.createStatement();
            st.setQueryTimeout(30);

            // 执行SQL语句
            ResultSet result = st.executeQuery(sql);
            ResultSetMetaData metaData = result.getMetaData();// 获取键名
            int columnCount = metaData.getColumnCount();// 获取行的数量

            while (result.next()) {
                Map<String, Object> map = new HashMap<String, Object>();
                for (int i = 0; i < columnCount; i++) {
                    String key = metaData.getColumnLabel(i + 1);
                    Object value = result.getObject(key);
                    map.put(key, value);
                }
                list.add(map);
            }
            return list;
        } catch (SQLException e) {
            LOGGER.error(e.getMessage());
            throw new RuntimeException("数据库查询失败");
        }
    }
}


