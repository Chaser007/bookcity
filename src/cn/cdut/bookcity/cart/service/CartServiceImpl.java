package cn.cdut.bookcity.cart.service;

import java.sql.SQLException;
import java.util.List;

import cn.cdut.bookcity.cart.dao.CartDaoImpl;
import cn.cdut.bookcity.cart.domain.CartItem;
import cn.cdut.util.common.CommonUtils;

/**
 * ���ﳵģ��ҵ���
 * @author huangyong
 * @date 2018��5��4�� ����9:02:38
 */
public class CartServiceImpl {

	private static CartDaoImpl cartDao = new CartDaoImpl();
	
	/**
	 * ͨ���û����ҹ��ﳵ��Ŀ��Ϣ 
	 * @param uid
	 * @return List<CartItem>   ��������
	 */
	public List<CartItem> findByUser(String uid){
		try {
			return cartDao.findByUser(uid);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * ��ӹ��ﳵ��Ŀ
	 * @param item 
	 * @return void   ��������
	 */
	public void addCartItem(CartItem item){
		try {
			String bid = item.getBook().getBid();
			String uid = item.getUser().getUid();
			//�����ݿ����õ���cartItem
			CartItem cartItem = cartDao.findByBookAndUser(bid, uid);
			/*
			 * �����ݿ��в����ڴ���Ŀ����Ӹ���Ŀ��
			 * ����ֻ�����乺��������
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
	 * ������Ŀ����,���Ҵ����ݿⷵ�ظ���Ŀ 
	 * @param id
	 * @param quantity
	 * @return CartItem   ��������
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
	 * ����ɾ�����ﳵ��Ŀ
	 * @param cartItemIds ��','Ӣ�Ķ��ŷָ���cartItemId�ַ��� 
	 * @return void   ��������
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
	 * ���ع��ﳵ��Ŀ 
	 * @param cartItemIds
	 * @return List<CartItem>   ��������
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
