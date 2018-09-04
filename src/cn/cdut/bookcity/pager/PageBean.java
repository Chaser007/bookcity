package cn.cdut.bookcity.pager;

import java.util.List;

/**
 * ҳ���ҳʵ����
 * @author huangyong
 * @date 2018��4��26�� ����10:11:07
 */
public class PageBean<T> {
	private int pageSize;		//һҳ��ʾ����Ŀ����һҳ�Ĵ�С
	private int pageCode;		//��ǰҳ
	private int totalRecords;	//�ܼ�¼��
	private String url;			//ҳ�������url�����ڱ���������������
	
	private List<T> beanList;	//ҳ������ʾ��JavaBean

	/**
	 * ������ҳ����ֱ��дgetter������Ҳ����ͨ��EL���ʽ��ȡ��
	 *  �൱����totalPage�������
	 * @return int   ��������
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
