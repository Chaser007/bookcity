package cn.cdut.bookcity.category.domain;

import java.util.List;

/**
 * 对应数据库的t_category表的实体类<br>
 * 该类是关系类
 * @author huangyong
 * @date 2018年4月25日 下午12:01:55
 */
public class CategoryBean {
	private String cid;
	private String cname;
	private CategoryBean parent;			//该目录的父目录
	private String desc;
	private List<CategoryBean> children;	//该目录的子目录
	
	public String getCid() {
		return cid;
	}
	public void setCid(String cid) {
		this.cid = cid;
	}
	public String getCname() {
		return cname;
	}
	public void setCname(String cname) {
		this.cname = cname;
	}
	public CategoryBean getParent() {
		return parent;
	}
	public void setParent(CategoryBean parent) {
		this.parent = parent;
	}
	public String getDesc() {
		return desc;
	}
	public void setDesc(String desc) {
		this.desc = desc;
	}
	public List<CategoryBean> getChildren() {
		return children;
	}
	public void setChildren(List<CategoryBean> children) {
		this.children = children;
	}
}
