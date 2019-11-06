package com.example.springbootutils.utils.dbpool;

import com.google.common.cache.*;
import lombok.extern.slf4j.Slf4j;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.concurrent.TimeUnit;

@Slf4j
public class ConnectionPoolUtils {
	private static LoadingCache<DBInfo, ConnectionPool> cache = null;

	private ConnectionPoolUtils() {

	}

	/**
	 * 
	 * @Description 通过DBInfo获取连接
	 *
	 */
	static Connection getConnection(DBInfo info) throws Exception {
		try {
			return getCache().get(info).getConnection();
		} catch (Exception e) {
			log.error("获取空闲数据库连接时出错: " + info, e);
			throw e;
		}
	}
	
	/**
	 * 
	 * @Description 通过DBInfo返回连接
	 *
	 */
	static void returnConnection(DBInfo info, Connection conn) {
		try {
			getCache().get(info).returnConnection(conn);
		} catch (Exception e) {
			log.error("返回空闲数据库连接时出错: " + info, e);
		}
	}

	/**
	 * 
	 * @Description 缓存数据库信息与连接池对应关系
	 * @return LoadingCache<DBInfo,ConnectionPool>
	 *
	 */
	private static LoadingCache<DBInfo, ConnectionPool> getCache() {
		if (cache == null) {
			synchronized (ConnectionPoolUtils.class) {
				if (cache == null) {
					cache = CacheBuilder
							.newBuilder()
							.maximumSize(100)
							//每30分钟刷新一下对应的连接池
							.expireAfterWrite(30, TimeUnit.MINUTES)
							.removalListener(new RemovalListener<DBInfo, ConnectionPool>() {
								@Override
								public void onRemoval(RemovalNotification<DBInfo, ConnectionPool> notification) {
									
									try {
										notification.getValue().closeConnectionPool();
										log.info(notification.getCause() + " 关闭连接池: " + notification.getKey());
									} catch (SQLException e) {
										log.error(notification.getKey() + "关闭连接池错误:", e);
									}
								}
								
							})
							.build(new CacheLoader<DBInfo, ConnectionPool>() {
								@Override
								public ConnectionPool load(DBInfo key) throws Exception {
									
									ConnectionPool pool = new ConnectionPool(key);
									try {
										pool.createPool();
										log.info("初始化连接池: " + key);
									} catch (Exception e) {
										log.error(key + "初始化连接池错误:", e);
									}
									return pool;
								}
					});
				}
			}
		}
		return cache;
	}
}
