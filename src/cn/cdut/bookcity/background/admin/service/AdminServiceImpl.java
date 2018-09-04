package cn.cdut.bookcity.background.admin.service;

import java.sql.SQLException;

import cn.cdut.bookcity.background.admin.dao.AdminDaoImpl;
import cn.cdut.bookcity.background.admin.domain.Admin;

/**
 * 后台的管理员模块的业务层
 * @author huangyong
 * @date 2018年5月13日 下午3:21:11
 */
public class AdminServiceImpl {
	
	private static AdminDaoImpl adminDao = new AdminDaoImpl();
	
	/**
	 * 用户登陆服务
	 * @param admin
	 * @return Admin   返回类型
	 */
	public Admin login(Admin admin){
		try {
			return adminDao.findByNameAndPass(admin.getAdminname(), admin.getAdminpwd());
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
}
