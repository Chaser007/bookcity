package cn.cdut.bookcity.background.admin.dao;

import java.sql.SQLException;

import org.apache.commons.dbutils.handlers.BeanHandler;

import cn.cdut.bookcity.background.admin.domain.Admin;
import cn.cdut.util.jdbc.TxQueryRunner;

/**
 * 后台的管理员模块的持久层
 * @author huangyong
 * @date 2018年5月13日 下午3:05:34
 */
public class AdminDaoImpl {

	private static TxQueryRunner qr = new TxQueryRunner();
	
	/**
	 * 根据登陆名和密码进行查询
	 * @param name
	 * @param pass
	 * @throws SQLException 
	 * @return Admin   返回类型
	 */
	public Admin findByNameAndPass(String name, String pass) throws SQLException{
		String sql = "SELECT * FROM t_admin WHERE adminname=? AND adminpwd=?";
		return qr.query(sql, new BeanHandler<Admin>(Admin.class), name, pass); 
	}
}
