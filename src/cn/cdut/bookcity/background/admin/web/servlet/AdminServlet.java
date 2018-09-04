package cn.cdut.bookcity.background.admin.web.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.cdut.bookcity.background.admin.domain.Admin;
import cn.cdut.bookcity.background.admin.service.AdminServiceImpl;
import cn.cdut.servlet.BaseServlet;
import cn.cdut.util.common.CommonUtils;

/**
 * ��̨�Ĺ���Աģ��Ŀ��Ʋ�
 * @author huangyong
 * @date 2018��5��13�� ����3:22:55
 */
public class AdminServlet extends BaseServlet {
	
	private static AdminServiceImpl adminService = new AdminServiceImpl();
	
	/**
	 * ����Ա��½���� 
	 * @param req
	 * @param resp
	 * @throws ServletException
	 * @throws IOException 
	 * @return String   ��������
	 */
	public String login(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		Admin form = CommonUtils.map2Bean(req.getParameterMap(), Admin.class);
		Admin admin = adminService.login(form);
		if(admin == null){
			req.setAttribute("msg", "�û������������!");
			return "f:/adminjsps/login.jsp";
		}
		req.getSession().setAttribute("admin", admin);
		return "r:/adminjsps/admin/main.jsp";
	}
}
