package com.example.springbootutils.utils.dbpool;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@EqualsAndHashCode
@ToString
@AllArgsConstructor
public class DBInfo {
    private String jdbcDriver; // 数据库驱动
    private String dbUrl; // 数据 URL
    private String dbUsername; // 数据库用户名
    private String dbPassword; // 数据库用户密码

    public enum DatabaseDriver {
        DRIVER_MYSQL("mysql", "com.mysql.jdbc.Driver"),
        DRIVER_ORACLE("oracle", "oracle.jdbc.driver.OracleDriver"),
        DRIVER_POSTGRESQL("postgresql", "org.postgresql.Driver"),
        ;

        private final String dbTypeName;
        private final String driverClassName;

        DatabaseDriver(String dbTypeName, String driverClassName) {
            this.dbTypeName = dbTypeName;
            this.driverClassName = driverClassName;
        }

        public static String getDriverClassName(String dbTypeName) {
            for (DatabaseDriver databaseDriver : DatabaseDriver.values()) {
                if (databaseDriver.dbTypeName.equalsIgnoreCase(dbTypeName)) {
                    return databaseDriver.driverClassName;
                }
            }
            return DRIVER_MYSQL.driverClassName;
        }
    }
}
