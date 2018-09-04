package cn.cdut.bookcity.order.domain;

import cn.cdut.bookcity.book.domain.BookBean;

/**
 * ������Ŀʵ����
 * @author huangyong
 * @date 2018��5��7�� ����9:08:28
 */
public class OrderItem {
	
	private String orderItemId;		//��Ŀid
	private int quantity;		//����Ŀ������
	private double subtotal;	//����Ŀ��С��
	private BookBean book;		//����Ŀ������Ϊ��
	private Order order;		//��������
	
	
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
