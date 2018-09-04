package cn.cdut.bookcity.order.domain;

import java.util.List;

import cn.cdut.bookcity.user.domain.UserBean;

/**
 * 订单实体类
 * @author huangyong
 * @date 2018年5月7日 下午9:08:13
 */
public class Order {
	
	private String oid;		//主键
	private String ordertime;	//订单生成时间
	private double total;		//订单总计
	private int status;		//订单状态--1:未支付、2:已支付未发货、3:已发货、4:确认收获、5:已取消
	private String address;		//收获地址
	private UserBean owner;		//订单所有者
	
	private List<OrderItem> orderItemList;	//订单包含的订单条目
	
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
