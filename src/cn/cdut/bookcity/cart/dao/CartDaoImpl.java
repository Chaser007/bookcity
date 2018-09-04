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
 * ���ﳵģ��־ò�
 * @author huangyong
 * @date 2018��5��4�� ����9:01:05
 */
public class CartDaoImpl {

	private static TxQueryRunner qr = new TxQueryRunner();
	
	/**
	 * ��map�е�����ӳ�䵽CartItemʵ������ 
	 * @param map
	 * @return CartItem   ��������
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
	 * ��һ��Map���͵�List����ת��ΪCartItem���͵�List����
	 * @param mapList 
	 * @return List<CartItem>   ��������
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
	 * ͨ���û����ҹ��ﳵ��Ŀ 
	 * @param uid
	 * @throws SQLException 
	 * @return List<CartItem>   ��������
	 */
	public List<CartItem> findByUser(String uid) throws SQLException{
		//���ϲ�ѯ
		String sql = "SELECT * FROM t_cartitem AS c,t_book AS b "
				+ "WHERE c.bid=b.bid AND c.uid=? ORDER BY c.orderBy";
		List<Map<String, Object>> mapList = qr.query(sql, new MapListHandler(), uid);
		return toCartItemList(mapList);
	}
	
	/**
	 * ��ӹ��ﳵ��Ŀ
	 * @param item
	 * @throws SQLException 
	 * @return void   ��������
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
	 * ͨ��ͼ����û����ҹ��ﳵ��Ŀ
	 * @param bid
	 * @return CartItem   ��������
	 * @throws SQLException 
	 */
	public CartItem findByBookAndUser(String bid, String uid) throws SQLException{
		String sql = "SELECT * FROM t_cartitem WHERE bid=? AND uid=?";
		Map<String, Object> map = qr.query(sql, new MapHandler(), bid, uid);
		return mapToCartItem(map);
	}
	
	/**
	 * ���¹��ﳵ��Ŀ�е�quantity�����ֶ�
	 * @param cartItemId
	 * @param quantity 
	 * @return void   ��������
	 * @throws SQLException 
	 */
	public void updateQuantity(String cartItemId, int quantity) throws SQLException{
		String sql = "UPDATE t_cartitem SET quantity=? WHERE cartItemId=?";
		qr.update(sql, quantity, cartItemId);
	}
	
	/**
	 * �����ﳵ��Ŀid��ѯ 
	 * @param id
	 * @throws SQLException 
	 * @return CartItem   ��������
	 */
	public CartItem findByCartItemId(String cartItemId) throws SQLException{
		String sql = "SELECT * FROM t_cartitem AS c,t_book AS b "
				+ "WHERE c.bid=b.bid AND c.cartItemId=?";
		return mapToCartItem(qr.query(sql, new MapHandler(),cartItemId));
	}
	
	/**
	 * ��������'cartItemId in(?,?)'���͵���� ,?�ĸ����ɲ���len����
	 * @param len �ʺŵĸ���
	 * @return String   ��������
	 */
	private String createSqlWhere(int len) {
		/*
		 * sqlWhereƴ�� 
		 */
		StringBuilder sqlWhere = new StringBuilder("cartItemId IN (");
		for(int i=0; i<len; i++){
			sqlWhere.append("?");
			if(i < len - 1){
				sqlWhere.append(",");
			}
		}
		//����sql���
		sqlWhere.append(")");
		return sqlWhere.toString();
	}
	
	/**
	 * ͨ�����ﳵ��Ŀ��������ɾ��
	 * @param cartItemIds cartItemId����
	 * @return void   ��������
	 * @throws SQLException 
	 */
	public void batchDelete(Object[] cartItemIds) throws SQLException{
		String sqlWhere = createSqlWhere(cartItemIds.length);
		String sql = "DELETE FROM t_cartitem WHERE " + sqlWhere;
		qr.update(sql, cartItemIds);
	}

	
	/**
	 * ��cartItemId����ָ������Ŀ 
	 * @param cartItemIds
	 * @throws SQLException 
	 * @return List<CartItem>   ��������
	 */
	public List<CartItem> loadCartItems(Object[] cartItemIds) throws SQLException{
		String sqlWhere = createSqlWhere(cartItemIds.length);
		String sql = "SELECT * FROM t_cartitem AS c, t_book AS b "
				+ "WHERE c.bid=b.bid AND " + sqlWhere;
		List<Map<String, Object>> mapList = qr.query(sql, new MapListHandler(), cartItemIds);
		return toCartItemList(mapList);
	}
}
