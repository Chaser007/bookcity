package cn.cdut.bookcity.background.admin.service;

import java.sql.SQLException;

import cn.cdut.bookcity.background.admin.dao.AdminDaoImpl;
import cn.cdut.bookcity.background.admin.domain.Admin;

/**
 * ��̨�Ĺ���Աģ���ҵ���
 * @author huangyong
 * @date 2018��5��13�� ����3:21:11
 */
public class AdminServiceImpl {
	
	private static AdminDaoImpl adminDao = new AdminDaoImpl();
	
	/**
	 * �û���½����
	 * @param admin
	 * @return Admin   ��������
	 */
	public Admin login(Admin admin){
		try {
			return adminDao.findByNameAndPass(admin.getAdminname(), admin.getAdminpwd());
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
}
