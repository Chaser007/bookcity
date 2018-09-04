package cn.cdut.bookcity.cart.domain;

import java.math.BigDecimal;

import cn.cdut.bookcity.book.domain.BookBean;
import cn.cdut.bookcity.user.domain.UserBean;

/**
 * 购物车条目实体类
 * @author huangyong
 * @date 2018年5月4日 下午9:07:08
 */
public class CartItem {

	private String cartItemId;	//条目id
	private int quantity;		//购买的数量
	private BookBean book;		//购买的书
	private UserBean user;		//购买人

	/**
	 * 得到每个条目的小计 
	 * @return String   返回类型
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
