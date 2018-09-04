package cn.cdut.bookcity.cart.service;

import java.sql.SQLException;
import java.util.List;

import cn.cdut.bookcity.cart.dao.CartDaoImpl;
import cn.cdut.bookcity.cart.domain.CartItem;
import cn.cdut.util.common.CommonUtils;

/**
 * 购物车模块业务层
 * @author huangyong
 * @date 2018年5月4日 下午9:02:38
 */
public class CartServiceImpl {

	private static CartDaoImpl cartDao = new CartDaoImpl();
	
	/**
	 * 通过用户查找购物车条目信息 
	 * @param uid
	 * @return List<CartItem>   返回类型
	 */
	public List<CartItem> findByUser(String uid){
		try {
			return cartDao.findByUser(uid);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * 添加购物车条目
	 * @param item 
	 * @return void   返回类型
	 */
	public void addCartItem(CartItem item){
		try {
			String bid = item.getBook().getBid();
			String uid = item.getUser().getUid();
			//从数据库中拿到的cartItem
			CartItem cartItem = cartDao.findByBookAndUser(bid, uid);
			/*
			 * 若数据库中不存在此条目则添加该条目，
			 * 否则只更新其购买数量。
			 */
			if(cartItem == null){
				item.setCartItemId(CommonUtils.uuid());
				cartDao.addCartItem(item);
			}else{
				int total = cartItem.getQuantity() + item.getQuantity();
				cartDao.updateQuantity(cartItem.getCartItemId(), total);
			}
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * 更新条目数量,并且从数据库返回该条目 
	 * @param id
	 * @param quantity
	 * @return CartItem   返回类型
	 */
	public CartItem updateQuantity(String cartItemId, int quantity){
		try {
			cartDao.updateQuantity(cartItemId, quantity);
			return cartDao.findByCartItemId(cartItemId);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * 批量删除购物车条目
	 * @param cartItemIds 用','英文逗号分隔的cartItemId字符串 
	 * @return void   返回类型
	 */
	public void batchDelete(String cartItemIds){
		try {
			Object[] params = cartItemIds.split(",");
			cartDao.batchDelete(params);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * 加载购物车条目 
	 * @param cartItemIds
	 * @return List<CartItem>   返回类型
	 */
	public List<CartItem> loadCartItems(String cartItemIds){
		try {
			Object[] params = cartItemIds.split(",");
			return cartDao.loadCartItems(params);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
}
