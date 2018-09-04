package cn.cdut.bookcity.order.service;

import java.sql.SQLException;

import cn.cdut.bookcity.order.dao.OrderDaoImpl;
import cn.cdut.bookcity.order.domain.Order;
import cn.cdut.bookcity.pager.PageBean;
import cn.cdut.util.jdbc.JdbcUtils;

/**
 * ����ģ��ҵ���
 * @author huangyong
 * @date 2018��5��7�� ����9:04:51
 */
public class OrderServiceImpl {

	private static OrderDaoImpl orderDao = new OrderDaoImpl();

	/**
	 * ���û���ѯ���ж���,����ҳ��ʾ 
	 * @param uid
	 * @param pc
	 * @return PageBean<Order>   ��������
	 */
	public PageBean<Order> findByUser(String uid, int pc) {
	 	try {
	 		JdbcUtils.beginTransaction();
	 		PageBean<Order> pageBean =  orderDao.findByUser(uid, pc);
			JdbcUtils.commitTransaction();
			return pageBean;
		} catch (SQLException e) {
			try {
				JdbcUtils.rollbackTransaction();
			} catch (SQLException e1) {}
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * ��oid����ָ������
	 * @param oid
	 * @return Order   ��������
	 */
	public Order loadOrder(String oid){
		try {
			JdbcUtils.beginTransaction();
			Order order = orderDao.loadOrder(oid);
			JdbcUtils.commitTransaction();
			return order;
		} catch (SQLException e) {
			try {
				JdbcUtils.rollbackTransaction();
			} catch (SQLException e1) {}
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * �����¶���
	 * @param order 
	 * @return void   ��������
	 */
	public void createOrder(Order order){
		try {
			JdbcUtils.beginTransaction();
			orderDao.addOrder(order);
			JdbcUtils.commitTransaction();
		} catch (SQLException e) {
			try {
				JdbcUtils.rollbackTransaction();
			} catch (SQLException e1) {}
			throw new RuntimeException();
		}
	}
	
	/**
	 * ���¶�����״̬��Ϣ
	 * @param status 
	 * @return void   ��������
	 */
	public void updateStatus(String oid, int status){
		try {
			orderDao.updateStatus(oid, status);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * ��ѯ������״̬��Ϣ 
	 * @param oid
	 * @return int   ��������
	 */
	public int findStatus(String oid){
		try {
			return orderDao.findStatus(oid);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * ��ѯ���ж��� 
	 * @param pc
	 * @return PageBean<Order>   ��������
	 */
	public PageBean<Order> findAll(int pc) {
		try {
			return orderDao.findAll(pc);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * ��״̬��ѯ����
	 * @param status
	 * @param pc
	 * @return PageBean<Order>   ��������
	 */
	public PageBean<Order> findByStatus(int status, int pc) {
		try {
			return orderDao.findByStatus(status, pc);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
}
