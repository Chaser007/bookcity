package cn.cdut.bookcity.user.dao;

import java.sql.SQLException;

import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;

import cn.cdut.bookcity.user.domain.UserBean;
import cn.cdut.util.jdbc.TxQueryRunner;
/**
 * �û�ģ��־ò�
 * @author huangyong
 * @date 2018��4��19�� ����11:56:10
 */
public class UserDaoImpl {
	
	private static TxQueryRunner qr = new TxQueryRunner();
	
	/**
	 * �����û�
	 * @param user
	 * @throws SQLException 
	 * @return void   ��������
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
	 * ����uid�������ѯ���м�¼����true 
	 * @param uid
	 * @param pass
	 * @throws SQLException 
	 * @return boolean   ��������
	 */
	public boolean findByUidAndPass(String uid, String pass) throws SQLException{
		String sql = "SELECT count(*) FROM t_user WHERE uid=? AND loginpass=?";
		Number count = (Number) qr.query(sql, new ScalarHandler(), uid, pass);
		return count.intValue() > 0;
	}
	
	/**
	 * ͨ��uid�����û�����
	 * @param uid
	 * @param newpass
	 * @throws SQLException 
	 * @return void   ��������
	 */
	public void updatePassword(String uid, String newpass) throws SQLException{
		String sql = "UPDATE t_user SET loginpass=? WHERE uid=?";
		qr.update(sql, newpass, uid);
	}
	
	/**
	 * ͨ���û������û������ѯ 
	 * @param name
	 * @param pass
	 * @throws SQLException 
	 * @return UserBean   ��������
	 */
	public UserBean findByLoginnameAndLoginPass(String name, String pass) throws SQLException{
		String sql = "SELECT * FROM t_user WHERE loginname=? AND loginpass=?";
		return qr.query(sql, new BeanHandler<UserBean>(UserBean.class), name, pass);
	}
	
	/**
	 * ͨ������������û� 
	 * @param code
	 * @throws SQLException 
	 * @return UserBean   ��������
	 */
	public UserBean findByActivationCode(String code) throws SQLException{
		String sql = "SELECT * FROM t_user WHERE activationCode=?";
		return qr.query(sql, new BeanHandler<UserBean>(UserBean.class), code);
	}
	
	/**
	 * �޸��û�����״̬
	 * @param uid
	 * @param status 
	 * @return void   ��������
	 * @throws SQLException 
	 */
	public void updateStatus(String uid, boolean status) throws SQLException{
		String sql = "UPDATE t_user SET status=? WHERE uid=?";
		qr.update(sql, status, uid);
	}
	
	/**
	 * �����û�����ѯ�����м�¼����true
	 * @param loginname
	 * @throws SQLException 
	 * @return boolean   ��������
	 */
	public boolean ajaxValidateLoginname(String loginname) throws SQLException{
		String sql = "SELECT count(*) from t_user WHERE loginname=?";
		Number count = (Number) qr.query(sql, new ScalarHandler(), loginname);
		return count.intValue() > 0;
	}
	
	/**
	 * ���������ַ��ѯ�����м�¼����true
	 * @param loginname
	 * @throws SQLException 
	 * @return boolean   ��������
	 */
	public boolean ajaxValidateEmail(String email) throws SQLException{
		String sql = "SELECT count(*) from t_user WHERE email=?";
		Number count = (Number) qr.query(sql, new ScalarHandler(), email);
		return count.intValue() > 0;
	}
}
