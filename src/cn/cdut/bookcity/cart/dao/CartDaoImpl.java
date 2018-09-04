package cn.cdut.bookcity.cart.dao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.dbutils.handlers.MapHandler;
import org.apache.commons.dbutils.handlers.MapListHandler;

import cn.cdut.bookcity.book.domain.BookBean;
import cn.cdut.bookcity.cart.domain.CartItem;
import cn.cdut.bookcity.user.domain.UserBean;
import cn.cdut.util.common.CommonUtils;
import cn.cdut.util.jdbc.TxQueryRunner;


/**
 * 购物车模块持久层
 * @author huangyong
 * @date 2018年5月4日 下午9:01:05
 */
public class CartDaoImpl {

	private static TxQueryRunner qr = new TxQueryRunner();
	
	/**
	 * 将map中的数据映射到CartItem实体类中 
	 * @param map
	 * @return CartItem   返回类型
	 */
	private CartItem mapToCartItem(Map<String, Object> map){
		if(map == null || map.isEmpty()){
			return null;
		}
		CartItem cartItem = CommonUtils.map2Bean(map, CartItem.class);
		BookBean book = CommonUtils.map2Bean(map, BookBean.class);
		UserBean user = CommonUtils.map2Bean(map, UserBean.class);
		cartItem.setBook(book);
		cartItem.setUser(user);
		return cartItem;
	}
	
	/**
	 * 将一个Map类型的List集合转换为CartItem类型的List集合
	 * @param mapList 
	 * @return List<CartItem>   返回类型
	 */
	private List<CartItem> toCartItemList(List<Map<String, Object>> mapList){
		if(mapList == null || mapList.isEmpty()){
			return null;
		}
		List<CartItem> cartItemList = new ArrayList<CartItem>();
		for(Map<String, Object> map : mapList){
			CartItem cartItem = mapToCartItem(map);
			cartItemList.add(cartItem);
		}
		return cartItemList;
	}
	
	/**
	 * 通过用户查找购物车条目 
	 * @param uid
	 * @throws SQLException 
	 * @return List<CartItem>   返回类型
	 */
	public List<CartItem> findByUser(String uid) throws SQLException{
		//联合查询
		String sql = "SELECT * FROM t_cartitem AS c,t_book AS b "
				+ "WHERE c.bid=b.bid AND c.uid=? ORDER BY c.orderBy";
		List<Map<String, Object>> mapList = qr.query(sql, new MapListHandler(), uid);
		return toCartItemList(mapList);
	}
	
	/**
	 * 添加购物车条目
	 * @param item
	 * @throws SQLException 
	 * @return void   返回类型
	 */
	public void addCartItem(CartItem item) throws SQLException{
		String sql = "INSERT INTO t_cartitem(cartItemId,quantity,bid,uid) VALUES(?,?,?,?)";
		Object[] params = {item.getCartItemId(),
						   item.getQuantity(),
						   item.getBook().getBid(),
						   item.getUser().getUid()};
		qr.update(sql, params);
	}
	
	/**
	 * 通过图书和用户查找购物车条目
	 * @param bid
	 * @return CartItem   返回类型
	 * @throws SQLException 
	 */
	public CartItem findByBookAndUser(String bid, String uid) throws SQLException{
		String sql = "SELECT * FROM t_cartitem WHERE bid=? AND uid=?";
		Map<String, Object> map = qr.query(sql, new MapHandler(), bid, uid);
		return mapToCartItem(map);
	}
	
	/**
	 * 更新购物车条目中的quantity数量字段
	 * @param cartItemId
	 * @param quantity 
	 * @return void   返回类型
	 * @throws SQLException 
	 */
	public void updateQuantity(String cartItemId, int quantity) throws SQLException{
		String sql = "UPDATE t_cartitem SET quantity=? WHERE cartItemId=?";
		qr.update(sql, quantity, cartItemId);
	}
	
	/**
	 * 按购物车条目id查询 
	 * @param id
	 * @throws SQLException 
	 * @return CartItem   返回类型
	 */
	public CartItem findByCartItemId(String cartItemId) throws SQLException{
		String sql = "SELECT * FROM t_cartitem AS c,t_book AS b "
				+ "WHERE c.bid=b.bid AND c.cartItemId=?";
		return mapToCartItem(qr.query(sql, new MapHandler(),cartItemId));
	}
	
	/**
	 * 用于生成'cartItemId in(?,?)'类型的语句 ,?的个数由参数len决定
	 * @param len 问号的个数
	 * @return String   返回类型
	 */
	private String createSqlWhere(int len) {
		/*
		 * sqlWhere拼接 
		 */
		StringBuilder sqlWhere = new StringBuilder("cartItemId IN (");
		for(int i=0; i<len; i++){
			sqlWhere.append("?");
			if(i < len - 1){
				sqlWhere.append(",");
			}
		}
		//完整sql语句
		sqlWhere.append(")");
		return sqlWhere.toString();
	}
	
	/**
	 * 通过购物车条目进行批量删除
	 * @param cartItemIds cartItemId数组
	 * @return void   返回类型
	 * @throws SQLException 
	 */
	public void batchDelete(Object[] cartItemIds) throws SQLException{
		String sqlWhere = createSqlWhere(cartItemIds.length);
		String sql = "DELETE FROM t_cartitem WHERE " + sqlWhere;
		qr.update(sql, cartItemIds);
	}

	
	/**
	 * 按cartItemId加载指定的条目 
	 * @param cartItemIds
	 * @throws SQLException 
	 * @return List<CartItem>   返回类型
	 */
	public List<CartItem> loadCartItems(Object[] cartItemIds) throws SQLException{
		String sqlWhere = createSqlWhere(cartItemIds.length);
		String sql = "SELECT * FROM t_cartitem AS c, t_book AS b "
				+ "WHERE c.bid=b.bid AND " + sqlWhere;
		List<Map<String, Object>> mapList = qr.query(sql, new MapListHandler(), cartItemIds);
		return toCartItemList(mapList);
	}
}
