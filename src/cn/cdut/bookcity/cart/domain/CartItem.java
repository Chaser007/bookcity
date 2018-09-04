package cn.cdut.bookcity.cart.domain;

import java.math.BigDecimal;

import cn.cdut.bookcity.book.domain.BookBean;
import cn.cdut.bookcity.user.domain.UserBean;

/**
 * ���ﳵ��Ŀʵ����
 * @author huangyong
 * @date 2018��5��4�� ����9:07:08
 */
public class CartItem {

	private String cartItemId;	//��Ŀid
	private int quantity;		//���������
	private BookBean book;		//�������
	private UserBean user;		//������

	/**
	 * �õ�ÿ����Ŀ��С�� 
	 * @return String   ��������
	 */
	public String getSubTotal(){
		BigDecimal price = new BigDecimal(String.valueOf(book.getCurrPrice()));
		BigDecimal subTotal = price.multiply(new BigDecimal(String.valueOf(quantity)));
		return subTotal.toString();
	}
	
	public String getCartItemId() {
		return cartItemId;
	}
	public void setCartItemId(String cartItemId) {
		this.cartItemId = cartItemId;
	}
	public int getQuantity() {
		return quantity;
	}
	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}
	public BookBean getBook() {
		return book;
	}
	public void setBook(BookBean book) {
		this.book = book;
	}
	public UserBean getUser() {
		return user;
	}
	public void setUser(UserBean user) {
		this.user = user;
	}
}
