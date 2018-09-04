package cn.cdut.bookcity.category.dao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.MapHandler;
import org.apache.commons.dbutils.handlers.MapListHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;

import cn.cdut.bookcity.category.domain.CategoryBean;
import cn.cdut.util.common.CommonUtils;
import cn.cdut.util.jdbc.TxQueryRunner;

/**
 * 分类模块持久层
 * @author huangyong
 * @date 2018年4月25日 上午11:52:45
 */
public class CategoryDaoImpl {
	
	private static TxQueryRunner qr = new TxQueryRunner();
	
	/**
	 * 从Map中封装数据到CategoryBean中 
	 * @param map
	 * @return CategoryBean   返回类型
	 */
	private CategoryBean mapToBean(Map<String, Object> map) {
		if(map == null || map.size() == 0){
			return null;
		}
		CategoryBean category = CommonUtils.map2Bean(map, CategoryBean.class);
		String pid = (String) map.get("pid");
		if(pid != null){
			CategoryBean parent = new CategoryBean();
			parent.setCid(pid);
			category.setParent(parent);
		}
		return category;
	}
	
	/**
	 * 封装List<Map<String, Object>>的数据到List<CategoryBean>
	 * @param mapList
	 * @return List<CategoryBean>   返回类型
	 */
	private List<CategoryBean> toCategoryBeanList(List<Map<String, Object>> mapList) {
		if(mapList == null || mapList.size() == 0){
			return null;
		}
		List<CategoryBean> categoryList = new ArrayList<CategoryBean>(); 
		for(Map<String, Object> map : mapList) {
			categoryList.add(mapToBean(map));
		}
		return categoryList;
	}
	
	/**
	 * 查找到所有的目录，并且封装到CategoryBean类型的List集合 
	 * @return List<CategoryBean>   返回类型
	 * @throws SQLException 
	 */
	public List<CategoryBean> findAllCategories() throws SQLException{
		String sql = "SELECT * FROM t_category WHERE pid IS NULL ORDER BY orderBy";
		//查找到所有的一级分类
		List<CategoryBean> categoryList = qr.query(sql, new BeanListHandler<CategoryBean>(CategoryBean.class));
		//遍历一级分类为其注入二级分类List集合
		for(CategoryBean parent : categoryList){
			//根据一级分类的cid查询属于其的二级分类
			List<CategoryBean> children = findByParentId(parent.getCid());
			parent.setChildren(children);
			//为每个二级分类注入其所属的一级分类
			for(CategoryBean child : children){
				child.setParent(parent);
			}
		}
		return categoryList;
	}

	/**
	 * 通过其所属父目录的pid查询
	 * @param pid
	 * @return List<CategoryBean>   返回类型
	 * @throws SQLException 
	 */
	public List<CategoryBean> findByParentId(String pid) throws SQLException{
		String sql = "SELECT * FROM t_category WHERE pid=? ORDER BY orderBy";
		List<Map<String, Object>> mapList = qr.query(sql, new MapListHandler(), pid);
		return toCategoryBeanList(mapList);
	}

	/**
	 * 添加分类,适用任意级分类
	 * @param category 
	 * @return void   返回类型
	 * @throws SQLException 
	 */
	public void addCategory(CategoryBean category) throws SQLException {
		//desc为mysql中的关键字,所以得打上``
		String sql = "INSERT INTO t_category(cid,cname,pid,`desc`) VALUES(?,?,?,?)";
		
		//用于判断是否为1级分类
		String pid = null;
		if(category.getParent() != null){
			pid = category.getParent().getCid();
		}
		
		Object[] params = {category.getCid(), category.getCname(),
				pid, category.getDesc()};
		qr.update(sql, params);
	}

	/**
	 * 查找所有一级分类,不带其子分类 
	 * @throws SQLException 
	 * @return List<CategoryBean>   返回类型
	 */
	public List<CategoryBean> findFirstLevel() throws SQLException {
		String sql = "SELECT * FROM t_category WHERE pid IS NULL ORDER BY orderBy";
		List<Map<String, Object>> mapList = qr.query(sql, new MapListHandler());
		return toCategoryBeanList(mapList);
	}

	/**
	 * 按cid查询分类,适用任意级分类
	 * @param cid
	 * @return CategoryBean   返回类型
	 * @throws SQLException 
	 */
	public CategoryBean load(String cid) throws SQLException {
		String sql = "SELECT * FROM t_category WHERE cid=?";
		Map<String, Object> map = qr.query(sql, new MapHandler(), cid);
		return mapToBean(map);
	}

	/**
	 * 修改分类,适用任意级分类
	 * @param category 
	 * @return void   返回类型
	 * @throws SQLException 
	 */
	public void modifyCategory(CategoryBean category) throws SQLException {
		String sql = "UPDATE t_category SET cname=?,`desc`=?,pid=? WHERE cid=?";
		
		String pid = null;
		//若其不为一级分类,则为其设置所属分类
		if(category.getParent() != null) {
			pid = category.getParent().getCid();
		}
		Object[] params = {category.getCname(), category.getDesc(),
				pid, category.getCid()};
		
		qr.update(sql, params);
	}

	/**
	 * 根据分类id查找其子分类个数 
	 * @param cid
	 * @return int   返回类型
	 * @throws SQLException 
	 */
	public int findChildNumByPid(String cid) throws SQLException {
		String sql = "SELECT count(*) FROM t_category WHERE pid=?";
		Number count = (Number) qr.query(sql, new ScalarHandler(), cid);
		return count.intValue();
	}

	/**
	 * 删除分类
	 * @param cid 
	 * @return void   返回类型
	 * @throws SQLException 
	 */
	public void deleteCategory(String cid) throws SQLException {
		String sql = "DELETE FROM t_category WHERE cid=?";
		qr.update(sql, cid);
	}
}
