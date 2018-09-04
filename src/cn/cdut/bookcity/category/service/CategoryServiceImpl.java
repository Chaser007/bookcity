package cn.cdut.bookcity.category.service;

import java.sql.SQLException;
import java.util.List;

import cn.cdut.bookcity.category.dao.CategoryDaoImpl;
import cn.cdut.bookcity.category.domain.CategoryBean;
import cn.cdut.util.jdbc.JdbcUtils;

/**
 * ����ģ��ķ����
 * @author huangyong
 * @date 2018��4��25�� ����11:56:32
 */
public class CategoryServiceImpl {

	private static CategoryDaoImpl categoryDao = new CategoryDaoImpl();
	
	/**
	 * �������з���ķ���,һ�������к��ж�������
	 * @return List<CategoryBean>   ��������
	 */
	public List<CategoryBean> findAllCategories(){
		try {
			JdbcUtils.beginTransaction();
			List<CategoryBean> categoryList = categoryDao.findAllCategories();
			JdbcUtils.commitTransaction();
			return categoryList;
		} catch (SQLException e) {
			try {
				JdbcUtils.rollbackTransaction();
			} catch (SQLException e1) {}
			throw new RuntimeException(e);
		}
	}

	/**
	 * ��ӷ���,�������⼶����
	 * @param parent 
	 * @return void   ��������
	 */
	public void addCategory(CategoryBean category) {
		try{
			categoryDao.addCategory(category);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * �������е�һ������
	 * @return List<CategoryBean>   ��������
	 */
	public List<CategoryBean> findFirstLevel() {
		try{
			return categoryDao.findFirstLevel();
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * ���ݸ�����Id�������ӷ��� 
	 * @param pid
	 * @return List<CategoryBean>   ��������
	 */
	public List<CategoryBean> findByParentId(String pid) {
		try {
			return categoryDao.findByParentId(pid);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * ����һ������,�������⼶����
	 * @param cid
	 * @return CategoryBean   ��������
	 */
	public CategoryBean load(String cid) {
		try{
			return categoryDao.load(cid);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * �޸ķ���,�������⼶����
	 * @param parent 
	 * @return void   ��������
	 */
	public void modifyCategory(CategoryBean category) {
		try{
			categoryDao.modifyCategory(category);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * ���ݷ���id�������ӷ������
	 * @param cid
	 * @return int   ��������
	 */
	public int findChildNumByPid(String cid) {
		try{
			return categoryDao.findChildNumByPid(cid);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * ɾ������,�������⼶����,���÷��������ӷ������,������쳣,��Ϊ���������
	 * @param cid 
	 * @return void   ��������
	 */
	public void deleteCategory(String cid) {
		try{
			categoryDao.deleteCategory(cid);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
}
