package com.example.springbootutils.utils.dbpool;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 单个数据库的连接池 提供三个方法 1.初始化连接池 2.获取连接  3.返回连接 4.关闭连接池
 */
@Slf4j
class ConnectionPool {
    private DBInfo dbInfo;

    private int initialConnections = 10; // 连接池的初始大小
    private int incrementalConnections = 5;// 连接池自动增加的大小
    private int maxConnections = 50; // 连接池最大的大小
    private List<PooledConnection> connections = null; // 存放连接池中数据库连接的向量 , 初始时为 null
    private AtomicInteger freeCount = new AtomicInteger(0);//空闲的数据连接

    /**
     * 构造函数
     *
     * @param DBInfo dbInfo  数据库信息驱动类串
     */
    ConnectionPool(DBInfo dbInfo) {
        this.dbInfo = dbInfo;
    }

    ConnectionPool(DBInfo dbInfo, int initialConnections,
                   int incrementalConnections, int maxConnections) {
        this.dbInfo = dbInfo;
        this.initialConnections = initialConnections;
        this.incrementalConnections = incrementalConnections;
        this.maxConnections = maxConnections;
    }

    /**
     * 创建一个数据库连接池，连接池中的可用连接的数量采用类成员 initialConnections 中设置的值
     */

    synchronized void createPool() throws Exception {
        if (connections != null) {
            return;
        }
        Class.forName(this.dbInfo.getJdbcDriver());
        connections = new ArrayList<>();
        createConnections(this.initialConnections);
    }

    /**
     * 创建由 numConnections 指定数目的数据库连接 , 并把这些连接 放入 connections 向量中
     *
     * @param numConnections 要创建的数据库连接的数目
     */

    private void createConnections(int numConnections) throws SQLException {
        // 循环创建指定数目的数据库连接
        for (int i = 0; i < numConnections; i++) {
            // 是否连接池中的数据库连接的数量己经达到最大
            if (this.maxConnections > 0 && this.connections.size() >= this.maxConnections) {
                break;
            }
            // 增加一个连接到连接池中（向量 connections 中）
            try {
                connections.add(new PooledConnection(newConnection()));
                freeCount.incrementAndGet();
            } catch (SQLException e) {
                log.error(this.dbInfo.getDbUrl() + " 创建数据库连接失败！" + e.getMessage());
                throw new SQLException();
            }
            log.debug(this.dbInfo.getDbUrl() + "增加了一个数据库连接，当前的数据库连接池大小为：" + connections.size());
            log.debug("当前数据库空闲连接数量为： " + freeCount.get());
        }
    }

    /**
     * 创建一个新的数据库连接并返回它
     *
     * @return 返回一个新创建的数据库连接
     */
    private Connection newConnection() throws SQLException {
        // 创建一个数据库连接
        Connection conn = DriverManager.getConnection(dbInfo.getDbUrl(), dbInfo.getDbUsername(), dbInfo.getDbPassword());
        return conn; // 返回创建的新的数据库连接
    }

    /**
     * 通过调用 getFreeConnection() 函数返回一个可用的数据库连接 , 如果当前没有可用的数据库连接，并且更多的数据库连接不能创
     * 建（如连接池大小的限制），此函数等待一会再尝试获取。
     *
     * @return 返回一个可用的数据库连接对象
     */

    synchronized Connection getConnection() throws SQLException {
        // 确保连接池己被创建
        if (connections == null) {
            return null; // 连接池还没创建，则返回 null
        }
        Connection conn = getFreeConnection();
        int i = 0;
        while (conn == null && i < 5) {
            wait(200);
            conn = getFreeConnection();
            i++;
        }
        return conn;
    }

    /**
     * 本函数从连接池向量 connections 中返回一个可用的的数据库连接，如果 当前没有可用的数据库连接，本函数则根据
     * incrementalConnections 设置 的值创建几个数据库连接，并放入连接池中。 如果创建后，所有的连接仍都在使用中，则返回 null
     *
     * @return 返回一个可用的数据库连接
     */
    private Connection getFreeConnection() throws SQLException {
        // 从连接池中获得一个可用的数据库连接
        Connection conn = findFreeConnection();
        if (conn == null) {
            // 如果目前连接池中没有可用的连接 创建一些连接
            createConnections(incrementalConnections);
            // 重新从池中查找是否有可用连接
            conn = findFreeConnection();
            if (conn == null) {
                // 如果创建连接后仍获得不到可用的连接，则返回 null
                return null;
            }
        }
        return conn;
    }

    /**
     * 查找连接池中所有的连接，查找一个可用的数据库连接， 如果没有可用的连接，返回 null
     *
     * @return 返回一个可用的数据库连接
     */
    private Connection findFreeConnection() throws SQLException {
        Connection conn = null;
        PooledConnection pConn = null;
        Iterator<PooledConnection> it = connections.iterator();
        while (it.hasNext()) {
            pConn = it.next();
            if (!pConn.isBusy()) {
                conn = pConn.getConnection();
                pConn.setBusy(true);
                freeCount.decrementAndGet();
                // 测试此连接是否可用
                if (!testConnection(conn)) {
                    try {
                        conn = newConnection();
                    } catch (SQLException e) {
                        log.error(this.dbInfo.getDbUrl() + " 创建数据库连接失败！" + e.getMessage());
                    }
                    pConn.setConnection(conn);
                }
                log.debug("从连接池获取到一个空闲连接 ，当前数据库连接数量为：" + connections.size() + "当前数据库空闲连接数量为： " + freeCount.get());
                break;
            }
        }
        return conn;
    }

    /**
     * 测试连接是否可用
     */
    private boolean testConnection(Connection conn) {
        try {
            Statement stmt = conn.createStatement();
            stmt.execute("select * from dual");
        } catch (SQLException e) {
            closeConnection(conn);
            return false;
        }
        return true;
    }

    /**
     * 此函数返回一个数据库连接到连接池中，并把此连接置为空闲。 所有使用连接池获得的数据库连接均应在不使用此连接时返回它。
     *
     * @param conn 需返回到连接池中的连接对象
     */

    void returnConnection(Connection conn) {
        // 确保连接池存在，如果连接没有创建（不存在），直接返回
        if (connections == null || conn == null) {
            return;
        }
        PooledConnection pConn;
        Iterator<PooledConnection> it = connections.iterator();
        while (it.hasNext()) {
            pConn = it.next();
            // 先找到连接池中的要返回的连接对象
            if (conn == pConn.getConnection()) {
                pConn.setBusy(false);
                freeCount.incrementAndGet();
                log.debug("返回到连接池一个空闲连接 ，当前数据库连接数量为：" + connections.size() + "当前数据库空闲连接数量为： " + freeCount.get());
                break;
            }
        }
    }

    /**
     * 刷新连接池中所有的连接对象
     */

    synchronized void refreshConnections() throws SQLException {
        // 确保连接池己创新存在
        if (connections == null) {
            return;
        }
        PooledConnection pConn = null;
        Iterator<PooledConnection> it = connections.iterator();
        while (it.hasNext()) {
            pConn = it.next();
            while (pConn.isBusy()) {
                wait(200);
            }
            closeConnection(pConn.getConnection());
            pConn.setConnection(newConnection());
            pConn.setBusy(false);
        }
    }

    /**
     * 关闭连接池中所有的连接，并清空连接池。
     */
    synchronized void closeConnectionPool() throws SQLException {
        // 确保连接池存在，如果不存在，返回
        if (connections == null) {
            return;
        }

        MyThread thread = new MyThread(connections);
        thread.start();
        connections = null;
    }

    /**
     * 关闭一个数据库连接
     */
    private void closeConnection(Connection conn) {
        try {
            conn.close();
            log.debug("关闭了一个数据库连接: " + this.dbInfo.getDbUrl());
        } catch (SQLException e) {
            log.error(this.dbInfo.getDbUrl() + " 关闭数据库连接出错： " + e.getMessage());
        }
    }

    /**
     * 使程序等待给定的毫秒数
     */
    private void wait(int mSeconds) {
        try {
            Thread.sleep(mSeconds);
        } catch (InterruptedException e) {

        }
    }

    /**
     * 内部使用的用于保存连接池中连接对象的类 此类中有两个成员，一个是数据库的连接，另一个是指示此连接是否 正在使用的标志。
     */

    @Data
    class PooledConnection {
        Connection connection;// 数据库连接
        boolean busy = false; // 此连接是否正在使用的标志，默认没有正在使用

        // 构造函数，根据一个 Connection 构告一个 PooledConnection 对象
        public PooledConnection(Connection connection) {
            this.connection = connection;
        }
    }

    /**
     * @ClassName: MyThread
     * @Description: 清理连接池使用的线程
     */
    class MyThread extends Thread {
        private List<PooledConnection> connections;

        public MyThread(List<PooledConnection> connections) {
            this.connections = connections;
        }

        @Override
        public void run() {
            log.info("关闭线程池");
            PooledConnection pConn;
            Iterator<PooledConnection> it = connections.iterator();
            while (it.hasNext()) {
                pConn = it.next();
                if (pConn.isBusy()) {
                    try {
                        wait(1000);
                    } catch (InterruptedException e) {
                        log.info(e.getMessage());
                    }
                }
                it.remove();
                closeConnection(pConn.getConnection());
            }
        }
    }

}
