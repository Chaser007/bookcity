package cn.cdut.bookcity.order.domain;

import java.util.List;

import cn.cdut.bookcity.user.domain.UserBean;

/**
 * ����ʵ����
 * @author huangyong
 * @date 2018��5��7�� ����9:08:13
 */
public class Order {
	
	private String oid;		//����
	private String ordertime;	//��������ʱ��
	private double total;		//�����ܼ�
	private int status;		//����״̬--1:δ֧����2:��֧��δ������3:�ѷ�����4:ȷ���ջ�5:��ȡ��
	private String address;		//�ջ��ַ
	private UserBean owner;		//����������
	
	private List<OrderItem> orderItemList;	//���������Ķ�����Ŀ
	
	public List<OrderItem> getOrderItemList() {
		return orderItemList;
	}
	public void setOrderItemList(List<OrderItem> orderItemList) {
		this.orderItemList = orderItemList;
	}
	
	public String getOid() {
		return oid;
	}
	public void setOid(String oid) {
		this.oid = oid;
	}
	public String getOrdertime() {
		return ordertime;
	}
	public void setOrdertime(String ordertime) {
		this.ordertime = ordertime;
	}
	public double getTotal() {
		return total;
	}
	public void setTotal(double total) {
		this.total = total;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public UserBean getOwner() {
		return owner;
	}
	public void setOwner(UserBean owner) {
		this.owner = owner;
	}

}
