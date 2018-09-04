package cn.cdut.bookcity.background.admin.dao;

import java.sql.SQLException;

import org.apache.commons.dbutils.handlers.BeanHandler;

import cn.cdut.bookcity.background.admin.domain.Admin;
import cn.cdut.util.jdbc.TxQueryRunner;

/**
 * ��̨�Ĺ���Աģ��ĳ־ò�
 * @author huangyong
 * @date 2018��5��13�� ����3:05:34
 */
public class AdminDaoImpl {

	private static TxQueryRunner qr = new TxQueryRunner();
	
	/**
	 * ���ݵ�½����������в�ѯ
	 * @param name
	 * @param pass
	 * @throws SQLException 
	 * @return Admin   ��������
	 */
	public Admin findByNameAndPass(String name, String pass) throws SQLException{
		String sql = "SELECT * FROM t_admin WHERE adminname=? AND adminpwd=?";
		return qr.query(sql, new BeanHandler<Admin>(Admin.class), name, pass); 
	}
}
