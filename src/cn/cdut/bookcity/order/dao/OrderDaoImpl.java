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
 * 订单模块持久层
 * @author huangyong
 * @date 2018年5月7日 下午9:03:00
 */
public class OrderDaoImpl {
	
	private static TxQueryRunner qr = new TxQueryRunner();
	
	/**
	 * 通用查询方法,多条件组合查询，所有条件都需满足
	 * @param criterion 表达式的集合
	 * @param pc 当前需要查询的页数
	 * @return PageBean<Order>   返回类型
	 * @throws SQLException 
	 */
	private PageBean<Order> queryByCriteria(List<Expression> criterion, int pc) throws SQLException{
		/*
		 * 1、设置分页大小
		 * 2、根据条件查询得到记录数
		 * 3、根据条件查询得到List<Book>
		 * 4、参数pc即为当前页
		 */
		//设置分页大小
		int pageSize = PageConstants.ORDER_PAGE_SIZE;
		
		/*
		 * where子句拼接,例如：
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
		
		//查询所有记录数
		String sql = "SELECT count(*) FROM t_order" + whereSql.toString();
		Number number = (Number) qr.query(sql, new ScalarHandler(), params.toArray());
		int totalRecords = number.intValue();
		
		//按条件查询
		sql = "SELECT * FROM t_order" + whereSql.toString() + " ORDER BY ordertime DESC LIMIT ?,?";
		//从第(pc - 1) * pageSize记录开始查，共查pageSize条记录
		//mysql中第一条记录是从0开始
		params.add((pc - 1) * pageSize);
		params.add(pageSize);
		List<Order> orderList = qr.query(sql, new BeanListHandler<Order>(Order.class), params.toArray());
		
		//为Order类关联OrderItem
		for(Order order : orderList){
			List<OrderItem> orderItemList = loadOrderItem(order.getOid());
			order.setOrderItemList(orderItemList);
		}
		
		//创建PageBean设置参数,其中url参数交给控制层设置
		PageBean<Order> pageBean =  new PageBean<Order>();
		pageBean.setPageCode(pc);
		pageBean.setPageSize(pageSize);
		pageBean.setTotalRecords(totalRecords);
		pageBean.setBeanList(orderList);
		
		return pageBean;
	}
	
	/**
	 * 将map中的结果映射到OrderItem 
	 * @param map
	 * @return OrderItem   返回类型
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
	 * 将List<Map<String, Object>>中的结果映射到List<OrderItem>集合中 
	 * @param mapList
	 * @return List<OrderItem>   返回类型
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
	  * 按订单的id(oid)来加载订单条目
	  * @param oid 所属订单id
	  * @throws SQLException 
	  * @return OrderItem   返回类型
	  */
	private List<OrderItem> loadOrderItem(String oid) throws SQLException{
		String sql = "SELECT * FROM t_orderitem WHERE oid=?";
		List<Map<String, Object>> mapList = qr.query(sql, new MapListHandler(), oid);
		return toOrderItemList(mapList);
	}
	
	/**
	 * 按用户查询所有订单,并分页显示 
	 * @param uid
	 * @param pc
	 * @return PageBean<Order>   返回类型
	 * @throws SQLException 
	 */
	public PageBean<Order> findByUser(String uid, int pc) throws SQLException {
		List<Expression> criterion = new ArrayList<Expression>();
		criterion.add(new Expression("uid", "=", uid));
		return queryByCriteria(criterion, pc);
	}
	
	/**
	 * 通过id加载订单
	 * @param oid
	 * @throws SQLException 
	 * @return Order   返回类型
	 */
	public Order loadOrder(String oid) throws SQLException{
		String sql = "SELECT * FROM t_order WHERE oid=?";
		Order order = qr.query(sql, new BeanHandler<Order>(Order.class), oid);
		order.setOrderItemList(loadOrderItem(oid));
		return order;
	}
	
	/**
	 * 添加订单
	 * @param order
	 * @throws SQLException 
	 * @return void   返回类型
	 */
	public void addOrder(Order order) throws SQLException{
		//向订单表中插入数据
		String sql = "INSERT INTO t_order(oid,ordertime,total,status,address,uid)"
				+ " VALUES(?,?,?,?,?,?)";
		Object[] orderParams = {order.getOid(), order.getOrdertime(),
				order.getTotal(), order.getStatus(), order.getAddress(),
				order.getOwner().getUid()};
		qr.update(sql, orderParams);
		
		//向订单条目表中插入数据
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
		//批处理
		qr.batch(sql, orderItemParams);
	}
	
	/**
	 * 修改订单的状态
	 * @param oid
	 * @param status
	 * @throws SQLException 
	 * @return void   返回类型
	 */
	public void updateStatus(String oid ,int status) throws SQLException{
		String sql = "UPDATE t_order SET status=? WHERE oid=?";
		qr.update(sql, status, oid);
	}

	/**
	 * 查询订单的状态 
	 * @param oid
	 * @throws SQLException 
	 * @return int   返回类型
	 */
	public int findStatus(String oid) throws SQLException{
		String sql = "SELECT status FROM t_order WHERE oid=?";
		Number number = (Number)qr.query(sql, new ScalarHandler(), oid);
		return number.intValue();
	}
	
	
	/**
	 * 查询所有订单
	 * @return PageBean<Order>   返回类型
	 * @throws SQLException 
	 */
	public PageBean<Order> findAll(int pc) throws SQLException {
		return queryByCriteria(null, pc);
	}
	
	/**
	 * 按订单状态查询 
	 * @param status
	 * @param pc
	 * @return PageBean<Order>   返回类型
	 * @throws SQLException 
	 */
	public PageBean<Order> findByStatus(int status, int pc) throws SQLException {
		List<Expression> criterion = new ArrayList<Expression>();
		criterion.add(new Expression("status", "=", status + ""));
		return queryByCriteria(criterion, pc);
	}
}
