package cn.cdut.bookcity.listener;

import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Enumeration;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import cn.cdut.util.jdbc.JdbcUtils;

/**
 * ������<br>
 * �ر�ComboPooledDataSource���ӳ�,ע������ע������
 * @author huangyong
 * @date 2018��4��23�� ����8:54:34
 */
public class CloseDataSourceListener implements ServletContextListener {

	@Override
	public void contextInitialized(ServletContextEvent arg0) {

	}

	@Override
	public void contextDestroyed(ServletContextEvent arg0) {
		JdbcUtils.closeDataSource();
		//ע������
		//�õ�����ע���˵�����
		Enumeration<Driver> drivers = DriverManager.getDrivers();
		//����ע��
		while(drivers.hasMoreElements()){
			try {
				DriverManager.deregisterDriver(drivers.nextElement());
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
}
