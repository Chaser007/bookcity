package cn.cdut.bookcity.order.domain;

import cn.cdut.bookcity.book.domain.BookBean;

/**
 * 订单条目实体类
 * @author huangyong
 * @date 2018年5月7日 下午9:08:28
 */
public class OrderItem {
	
	private String orderItemId;		//条目id
	private int quantity;		//该条目的数量
	private double subtotal;	//该条目的小计
	private BookBean book;		//该条目的内容为书
	private Order order;		//所属订单
	
	
	public String getOrderItemId() {
		return orderItemId;
	}
	public void setOrderItemId(String orderItemId) {
		this.orderItemId = orderItemId;
	}
	public int getQuantity() {
		return quantity;
	}
	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}
	public double getSubtotal() {
		return subtotal;
	}
	public void setSubtotal(double subtotal) {
		this.subtotal = subtotal;
	}
	public BookBean getBook() {
		return book;
	}
	public void setBook(BookBean book) {
		this.book = book;
	}
	public Order getOrder() {
		return order;
	}
	public void setOrder(Order order) {
		this.order = order;
	}
}
