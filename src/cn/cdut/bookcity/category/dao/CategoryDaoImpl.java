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
 * ����ģ��־ò�
 * @author huangyong
 * @date 2018��4��25�� ����11:52:45
 */
public class CategoryDaoImpl {
	
	private static TxQueryRunner qr = new TxQueryRunner();
	
	/**
	 * ��Map�з�װ���ݵ�CategoryBean�� 
	 * @param map
	 * @return CategoryBean   ��������
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
	 * ��װList<Map<String, Object>>�����ݵ�List<CategoryBean>
	 * @param mapList
	 * @return List<CategoryBean>   ��������
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
	 * ���ҵ����е�Ŀ¼�����ҷ�װ��CategoryBean���͵�List���� 
	 * @return List<CategoryBean>   ��������
	 * @throws SQLException 
	 */
	public List<CategoryBean> findAllCategories() throws SQLException{
		String sql = "SELECT * FROM t_category WHERE pid IS NULL ORDER BY orderBy";
		//���ҵ����е�һ������
		List<CategoryBean> categoryList = qr.query(sql, new BeanListHandler<CategoryBean>(CategoryBean.class));
		//����һ������Ϊ��ע���������List����
		for(CategoryBean parent : categoryList){
			//����һ�������cid��ѯ������Ķ�������
			List<CategoryBean> children = findByParentId(parent.getCid());
			parent.setChildren(children);
			//Ϊÿ����������ע����������һ������
			for(CategoryBean child : children){
				child.setParent(parent);
			}
		}
		return categoryList;
	}

	/**
	 * ͨ����������Ŀ¼��pid��ѯ
	 * @param pid
	 * @return List<CategoryBean>   ��������
	 * @throws SQLException 
	 */
	public List<CategoryBean> findByParentId(String pid) throws SQLException{
		String sql = "SELECT * FROM t_category WHERE pid=? ORDER BY orderBy";
		List<Map<String, Object>> mapList = qr.query(sql, new MapListHandler(), pid);
		return toCategoryBeanList(mapList);
	}

	/**
	 * ��ӷ���,�������⼶����
	 * @param category 
	 * @return void   ��������
	 * @throws SQLException 
	 */
	public void addCategory(CategoryBean category) throws SQLException {
		//descΪmysql�еĹؼ���,���Եô���``
		String sql = "INSERT INTO t_category(cid,cname,pid,`desc`) VALUES(?,?,?,?)";
		
		//�����ж��Ƿ�Ϊ1������
		String pid = null;
		if(category.getParent() != null){
			pid = category.getParent().getCid();
		}
		
		Object[] params = {category.getCid(), category.getCname(),
				pid, category.getDesc()};
		qr.update(sql, params);
	}

	/**
	 * ��������һ������,�������ӷ��� 
	 * @throws SQLException 
	 * @return List<CategoryBean>   ��������
	 */
	public List<CategoryBean> findFirstLevel() throws SQLException {
		String sql = "SELECT * FROM t_category WHERE pid IS NULL ORDER BY orderBy";
		List<Map<String, Object>> mapList = qr.query(sql, new MapListHandler());
		return toCategoryBeanList(mapList);
	}

	/**
	 * ��cid��ѯ����,�������⼶����
	 * @param cid
	 * @return CategoryBean   ��������
	 * @throws SQLException 
	 */
	public CategoryBean load(String cid) throws SQLException {
		String sql = "SELECT * FROM t_category WHERE cid=?";
		Map<String, Object> map = qr.query(sql, new MapHandler(), cid);
		return mapToBean(map);
	}

	/**
	 * �޸ķ���,�������⼶����
	 * @param category 
	 * @return void   ��������
	 * @throws SQLException 
	 */
	public void modifyCategory(CategoryBean category) throws SQLException {
		String sql = "UPDATE t_category SET cname=?,`desc`=?,pid=? WHERE cid=?";
		
		String pid = null;
		//���䲻Ϊһ������,��Ϊ��������������
		if(category.getParent() != null) {
			pid = category.getParent().getCid();
		}
		Object[] params = {category.getCname(), category.getDesc(),
				pid, category.getCid()};
		
		qr.update(sql, params);
	}

	/**
	 * ���ݷ���id�������ӷ������ 
	 * @param cid
	 * @return int   ��������
	 * @throws SQLException 
	 */
	public int findChildNumByPid(String cid) throws SQLException {
		String sql = "SELECT count(*) FROM t_category WHERE pid=?";
		Number count = (Number) qr.query(sql, new ScalarHandler(), cid);
		return count.intValue();
	}

	/**
	 * ɾ������
	 * @param cid 
	 * @return void   ��������
	 * @throws SQLException 
	 */
	public void deleteCategory(String cid) throws SQLException {
		String sql = "DELETE FROM t_category WHERE cid=?";
		qr.update(sql, cid);
	}
}
