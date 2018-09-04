package cn.cdut.bookcity.user.dao;

import java.sql.SQLException;

import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;

import cn.cdut.bookcity.user.domain.UserBean;
import cn.cdut.util.jdbc.TxQueryRunner;
/**
 * 用户模块持久层
 * @author huangyong
 * @date 2018年4月19日 上午11:56:10
 */
public class UserDaoImpl {
	
	private static TxQueryRunner qr = new TxQueryRunner();
	
	/**
	 * 新增用户
	 * @param user
	 * @throws SQLException 
	 * @return void   返回类型
	 */
	public void addUser(UserBean user) throws SQLException{
		String sql = "INSERT INTO t_user(uid,loginname,loginpass,email,status,activationCode) "
				+ "VALUES(?,?,?,?,?,?)";
		Object params[] = {user.getUid(),user.getLoginname(),
						   user.getLoginpass(),user.getEmail(),
						   user.isStatus(),user.getActivationCode()};
		qr.update(sql, params);
	}
	
	/**
	 * 根据uid和密码查询，有记录返回true 
	 * @param uid
	 * @param pass
	 * @throws SQLException 
	 * @return boolean   返回类型
	 */
	public boolean findByUidAndPass(String uid, String pass) throws SQLException{
		String sql = "SELECT count(*) FROM t_user WHERE uid=? AND loginpass=?";
		Number count = (Number) qr.query(sql, new ScalarHandler(), uid, pass);
		return count.intValue() > 0;
	}
	
	/**
	 * 通过uid更新用户密码
	 * @param uid
	 * @param newpass
	 * @throws SQLException 
	 * @return void   返回类型
	 */
	public void updatePassword(String uid, String newpass) throws SQLException{
		String sql = "UPDATE t_user SET loginpass=? WHERE uid=?";
		qr.update(sql, newpass, uid);
	}
	
	/**
	 * 通过用户名和用户密码查询 
	 * @param name
	 * @param pass
	 * @throws SQLException 
	 * @return UserBean   返回类型
	 */
	public UserBean findByLoginnameAndLoginPass(String name, String pass) throws SQLException{
		String sql = "SELECT * FROM t_user WHERE loginname=? AND loginpass=?";
		return qr.query(sql, new BeanHandler<UserBean>(UserBean.class), name, pass);
	}
	
	/**
	 * 通过激活码查找用户 
	 * @param code
	 * @throws SQLException 
	 * @return UserBean   返回类型
	 */
	public UserBean findByActivationCode(String code) throws SQLException{
		String sql = "SELECT * FROM t_user WHERE activationCode=?";
		return qr.query(sql, new BeanHandler<UserBean>(UserBean.class), code);
	}
	
	/**
	 * 修改用户激活状态
	 * @param uid
	 * @param status 
	 * @return void   返回类型
	 * @throws SQLException 
	 */
	public void updateStatus(String uid, boolean status) throws SQLException{
		String sql = "UPDATE t_user SET status=? WHERE uid=?";
		qr.update(sql, status, uid);
	}
	
	/**
	 * 根据用户名查询，若有记录返回true
	 * @param loginname
	 * @throws SQLException 
	 * @return boolean   返回类型
	 */
	public boolean ajaxValidateLoginname(String loginname) throws SQLException{
		String sql = "SELECT count(*) from t_user WHERE loginname=?";
		Number count = (Number) qr.query(sql, new ScalarHandler(), loginname);
		return count.intValue() > 0;
	}
	
	/**
	 * 根据邮箱地址查询，若有记录返回true
	 * @param loginname
	 * @throws SQLException 
	 * @return boolean   返回类型
	 */
	public boolean ajaxValidateEmail(String email) throws SQLException{
		String sql = "SELECT count(*) from t_user WHERE email=?";
		Number count = (Number) qr.query(sql, new ScalarHandler(), email);
		return count.intValue() > 0;
	}
}
