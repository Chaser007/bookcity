package cn.cdut.bookcity.pager;

import java.util.List;

/**
 * 页面分页实体类
 * @author huangyong
 * @date 2018年4月26日 下午10:11:07
 */
public class PageBean<T> {
	private int pageSize;		//一页显示的数目，即一页的大小
	private int pageCode;		//当前页
	private int totalRecords;	//总记录数
	private String url;			//页面请求的url，用于保持搜索条件不变
	
	private List<T> beanList;	//页面中显示的JavaBean

	/**
	 * 返回总页数，直接写getter方法，也可以通过EL表达式获取，
	 *  相当于有totalPage这个属性
	 * @return int   返回类型
	 */
	public int getTotalPage(){
		int totalPage = totalRecords / pageSize;
		return (totalRecords % pageSize) == 0 ? totalPage : totalPage + 1;
	}
	
	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public int getPageCode() {
		return pageCode;
	}

	public void setPageCode(int pageCode) {
		this.pageCode = pageCode;
	}

	public int getTotalRecords() {
		return totalRecords;
	}

	public void setTotalRecords(int totalRecords) {
		this.totalRecords = totalRecords;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public List<T> getBeanList() {
		return beanList;
	}

	public void setBeanList(List<T> beanList) {
		this.beanList = beanList;
	}
}
