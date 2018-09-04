package cn.cdut.bookcity.category.service;

import java.sql.SQLException;
import java.util.List;

import cn.cdut.bookcity.category.dao.CategoryDaoImpl;
import cn.cdut.bookcity.category.domain.CategoryBean;
import cn.cdut.util.jdbc.JdbcUtils;

/**
 * 分类模块的服务层
 * @author huangyong
 * @date 2018年4月25日 上午11:56:32
 */
public class CategoryServiceImpl {

	private static CategoryDaoImpl categoryDao = new CategoryDaoImpl();
	
	/**
	 * 返回所有分类的服务,一级分类中含有二级分类
	 * @return List<CategoryBean>   返回类型
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
	 * 添加分类,适用任意级分类
	 * @param parent 
	 * @return void   返回类型
	 */
	public void addCategory(CategoryBean category) {
		try{
			categoryDao.addCategory(category);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * 查找所有的一级分类
	 * @return List<CategoryBean>   返回类型
	 */
	public List<CategoryBean> findFirstLevel() {
		try{
			return categoryDao.findFirstLevel();
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * 根据父分类Id查找其子分类 
	 * @param pid
	 * @return List<CategoryBean>   返回类型
	 */
	public List<CategoryBean> findByParentId(String pid) {
		try {
			return categoryDao.findByParentId(pid);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * 加载一个分类,适用任意级分类
	 * @param cid
	 * @return CategoryBean   返回类型
	 */
	public CategoryBean load(String cid) {
		try{
			return categoryDao.load(cid);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * 修改分类,适用任意级分类
	 * @param parent 
	 * @return void   返回类型
	 */
	public void modifyCategory(CategoryBean category) {
		try{
			categoryDao.modifyCategory(category);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * 根据分类id查找其子分类个数
	 * @param cid
	 * @return int   返回类型
	 */
	public int findChildNumByPid(String cid) {
		try{
			return categoryDao.findChildNumByPid(cid);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * 删除分类,适用任意级分类,若该分类下有子分类或书,则会抛异常,因为有外键关联
	 * @param cid 
	 * @return void   返回类型
	 */
	public void deleteCategory(String cid) {
		try{
			categoryDao.deleteCategory(cid);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
}
