package cn.cdut.bookcity.order.service;

import java.sql.SQLException;

import cn.cdut.bookcity.order.dao.OrderDaoImpl;
import cn.cdut.bookcity.order.domain.Order;
import cn.cdut.bookcity.pager.PageBean;
import cn.cdut.util.jdbc.JdbcUtils;

/**
 * 订单模块业务层
 * @author huangyong
 * @date 2018年5月7日 下午9:04:51
 */
public class OrderServiceImpl {

	private static OrderDaoImpl orderDao = new OrderDaoImpl();

	/**
	 * 按用户查询所有订单,并分页显示 
	 * @param uid
	 * @param pc
	 * @return PageBean<Order>   返回类型
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
	 * 按oid加载指定订单
	 * @param oid
	 * @return Order   返回类型
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
	 * 创建新订单
	 * @param order 
	 * @return void   返回类型
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
	 * 更新订单的状态信息
	 * @param status 
	 * @return void   返回类型
	 */
	public void updateStatus(String oid, int status){
		try {
			orderDao.updateStatus(oid, status);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * 查询订单的状态信息 
	 * @param oid
	 * @return int   返回类型
	 */
	public int findStatus(String oid){
		try {
			return orderDao.findStatus(oid);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * 查询所有订单 
	 * @param pc
	 * @return PageBean<Order>   返回类型
	 */
	public PageBean<Order> findAll(int pc) {
		try {
			return orderDao.findAll(pc);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * 按状态查询订单
	 * @param status
	 * @param pc
	 * @return PageBean<Order>   返回类型
	 */
	public PageBean<Order> findByStatus(int status, int pc) {
		try {
			return orderDao.findByStatus(status, pc);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
}
