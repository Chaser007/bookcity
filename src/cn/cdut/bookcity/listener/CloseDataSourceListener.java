package cn.cdut.bookcity.listener;

import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Enumeration;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import cn.cdut.util.jdbc.JdbcUtils;

/**
 * 监听器<br>
 * 关闭ComboPooledDataSource连接池,注销所有注册驱动
 * @author huangyong
 * @date 2018年4月23日 下午8:54:34
 */
public class CloseDataSourceListener implements ServletContextListener {

	@Override
	public void contextInitialized(ServletContextEvent arg0) {

	}

	@Override
	public void contextDestroyed(ServletContextEvent arg0) {
		JdbcUtils.closeDataSource();
		//注销驱动
		//得到所有注册了的驱动
		Enumeration<Driver> drivers = DriverManager.getDrivers();
		//遍历注销
		while(drivers.hasMoreElements()){
			try {
				DriverManager.deregisterDriver(drivers.nextElement());
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
}
