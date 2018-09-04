package cn.cdut.bookcity.order.dao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.MapListHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;

import cn.cdut.bookcity.book.domain.BookBean;
import cn.cdut.bookcity.expression.Expression;
import cn.cdut.bookcity.order.domain.Order;
import cn.cdut.bookcity.order.domain.OrderItem;
import cn.cdut.bookcity.pager.PageBean;
import cn.cdut.bookcity.pager.PageConstants;
import cn.cdut.util.common.CommonUtils;
import cn.cdut.util.jdbc.TxQueryRunner;

/**
 * ����ģ��־ò�
 * @author huangyong
 * @date 2018��5��7�� ����9:03:00
 */
public class OrderDaoImpl {
	
	private static TxQueryRunner qr = new TxQueryRunner();
	
	/**
	 * ͨ�ò�ѯ����,��������ϲ�ѯ������������������
	 * @param criterion ���ʽ�ļ���
	 * @param pc ��ǰ��Ҫ��ѯ��ҳ��
	 * @return PageBean<Order>   ��������
	 * @throws SQLException 
	 */
	private PageBean<Order> queryByCriteria(List<Expression> criterion, int pc) throws SQLException{
		/*
		 * 1�����÷�ҳ��С
		 * 2������������ѯ�õ���¼��
		 * 3������������ѯ�õ�List<Book>
		 * 4������pc��Ϊ��ǰҳ
		 */
		//���÷�ҳ��С
		int pageSize = PageConstants.ORDER_PAGE_SIZE;
		
		/*
		 * where�Ӿ�ƴ��,���磺
		 *  WHERE 1=1 AND id = ? AND name = ? AND age is not null
		 */
		StringBuilder whereSql = new StringBuilder(" WHERE 1=1");
		List<Object> params = new ArrayList<Object>();
		if(criterion != null) {
			for(Expression exp : criterion){
				whereSql.append(" ").append("AND").append(" ").append(exp.getName()).
						 append(" ").append(exp.getOperator());
				if(exp.getValue() != null && exp.getValue().trim() != "" ){
					whereSql.append(" ").append("?");
					params.add(exp.getValue());
				}
			}
		}
		
		//��ѯ���м�¼��
		String sql = "SELECT count(*) FROM t_order" + whereSql.toString();
		Number number = (Number) qr.query(sql, new ScalarHandler(), params.toArray());
		int totalRecords = number.intValue();
		
		//��������ѯ
		sql = "SELECT * FROM t_order" + whereSql.toString() + " ORDER BY ordertime DESC LIMIT ?,?";
		//�ӵ�(pc - 1) * pageSize��¼��ʼ�飬����pageSize����¼
		//mysql�е�һ����¼�Ǵ�0��ʼ
		params.add((pc - 1) * pageSize);
		params.add(pageSize);
		List<Order> orderList = qr.query(sql, new BeanListHandler<Order>(Order.class), params.toArray());
		
		//ΪOrder�����OrderItem
		for(Order order : orderList){
			List<OrderItem> orderItemList = loadOrderItem(order.getOid());
			order.setOrderItemList(orderItemList);
		}
		
		//����PageBean���ò���,����url�����������Ʋ�����
		PageBean<Order> pageBean =  new PageBean<Order>();
		pageBean.setPageCode(pc);
		pageBean.setPageSize(pageSize);
		pageBean.setTotalRecords(totalRecords);
		pageBean.setBeanList(orderList);
		
		return pageBean;
	}
	
	/**
	 * ��map�еĽ��ӳ�䵽OrderItem 
	 * @param map
	 * @return OrderItem   ��������
	 */
	private OrderItem mapToOrderItem(Map<String, Object> map){
		if(map == null || map.size() == 0)
			return null;
		OrderItem orderItem = CommonUtils.map2Bean(map, OrderItem.class);
		BookBean book = CommonUtils.map2Bean(map, BookBean.class);
		orderItem.setBook(book);
		return orderItem;
	}
	
	/**
	 * ��List<Map<String, Object>>�еĽ��ӳ�䵽List<OrderItem>������ 
	 * @param mapList
	 * @return List<OrderItem>   ��������
	 */
	private List<OrderItem> toOrderItemList(List<Map<String, Object>> mapList){
		if(mapList == null || mapList.size() == 0)
			return null;
		List<OrderItem> orderItemList = new ArrayList<OrderItem>();
		for(Map<String, Object> map : mapList){
			orderItemList.add(mapToOrderItem(map));
		}
		return orderItemList;
	}
	
	 /**
	  * ��������id(oid)�����ض�����Ŀ
	  * @param oid ��������id
	  * @throws SQLException 
	  * @return OrderItem   ��������
	  */
	private List<OrderItem> loadOrderItem(String oid) throws SQLException{
		String sql = "SELECT * FROM t_orderitem WHERE oid=?";
		List<Map<String, Object>> mapList = qr.query(sql, new MapListHandler(), oid);
		return toOrderItemList(mapList);
	}
	
	/**
	 * ���û���ѯ���ж���,����ҳ��ʾ 
	 * @param uid
	 * @param pc
	 * @return PageBean<Order>   ��������
	 * @throws SQLException 
	 */
	public PageBean<Order> findByUser(String uid, int pc) throws SQLException {
		List<Expression> criterion = new ArrayList<Expression>();
		criterion.add(new Expression("uid", "=", uid));
		return queryByCriteria(criterion, pc);
	}
	
	/**
	 * ͨ��id���ض���
	 * @param oid
	 * @throws SQLException 
	 * @return Order   ��������
	 */
	public Order loadOrder(String oid) throws SQLException{
		String sql = "SELECT * FROM t_order WHERE oid=?";
		Order order = qr.query(sql, new BeanHandler<Order>(Order.class), oid);
		order.setOrderItemList(loadOrderItem(oid));
		return order;
	}
	
	/**
	 * ��Ӷ���
	 * @param order
	 * @throws SQLException 
	 * @return void   ��������
	 */
	public void addOrder(Order order) throws SQLException{
		//�򶩵����в�������
		String sql = "INSERT INTO t_order(oid,ordertime,total,status,address,uid)"
				+ " VALUES(?,?,?,?,?,?)";
		Object[] orderParams = {order.getOid(), order.getOrdertime(),
				order.getTotal(), order.getStatus(), order.getAddress(),
				order.getOwner().getUid()};
		qr.update(sql, orderParams);
		
		//�򶩵���Ŀ���в�������
		sql = "INSERT INTO t_orderitem(orderItemId,quantity,subtotal,bid,bname,"
				+ "currPrice,image_b,oid) VALUES(?,?,?,?,?,?,?,?)";
		List<OrderItem> orderItemList = order.getOrderItemList();
		int len = orderItemList.size();
		Object[][] orderItemParams = new Object[len][];
		OrderItem orderItem = null;
		for(int i=0; i<len; i++){
			orderItem = orderItemList.get(i);
			orderItemParams[i] = new Object[]{orderItem.getOrderItemId(),
					orderItem.getQuantity(), orderItem.getSubtotal(),
					orderItem.getBook().getBid(), orderItem.getBook().getBname(),
					orderItem.getBook().getCurrPrice(), orderItem.getBook().getImage_b(),
					orderItem.getOrder().getOid()};
		}
		//������
		qr.batch(sql, orderItemParams);
	}
	
	/**
	 * �޸Ķ�����״̬
	 * @param oid
	 * @param status
	 * @throws SQLException 
	 * @return void   ��������
	 */
	public void updateStatus(String oid ,int status) throws SQLException{
		String sql = "UPDATE t_order SET status=? WHERE oid=?";
		qr.update(sql, status, oid);
	}

	/**
	 * ��ѯ������״̬ 
	 * @param oid
	 * @throws SQLException 
	 * @return int   ��������
	 */
	public int findStatus(String oid) throws SQLException{
		String sql = "SELECT status FROM t_order WHERE oid=?";
		Number number = (Number)qr.query(sql, new ScalarHandler(), oid);
		return number.intValue();
	}
	
	
	/**
	 * ��ѯ���ж���
	 * @return PageBean<Order>   ��������
	 * @throws SQLException 
	 */
	public PageBean<Order> findAll(int pc) throws SQLException {
		return queryByCriteria(null, pc);
	}
	
	/**
	 * ������״̬��ѯ 
	 * @param status
	 * @param pc
	 * @return PageBean<Order>   ��������
	 * @throws SQLException 
	 */
	public PageBean<Order> findByStatus(int status, int pc) throws SQLException {
		List<Expression> criterion = new ArrayList<Expression>();
		criterion.add(new Expression("status", "=", status + ""));
		return queryByCriteria(criterion, pc);
	}
}
