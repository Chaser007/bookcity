package cn.cdut.bookcity.category.domain;

import java.util.List;

/**
 * ��Ӧ���ݿ��t_category���ʵ����<br>
 * �����ǹ�ϵ��
 * @author huangyong
 * @date 2018��4��25�� ����12:01:55
 */
public class CategoryBean {
	private String cid;
	private String cname;
	private CategoryBean parent;			//��Ŀ¼�ĸ�Ŀ¼
	private String desc;
	private List<CategoryBean> children;	//��Ŀ¼����Ŀ¼
	
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
