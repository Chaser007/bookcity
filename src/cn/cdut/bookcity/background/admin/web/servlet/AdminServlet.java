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
 * 后台的管理员模块的控制层
 * @author huangyong
 * @date 2018年5月13日 下午3:22:55
 */
public class AdminServlet extends BaseServlet {
	
	private static AdminServiceImpl adminService = new AdminServiceImpl();
	
	/**
	 * 管理员登陆功能 
	 * @param req
	 * @param resp
	 * @throws ServletException
	 * @throws IOException 
	 * @return String   返回类型
	 */
	public String login(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		Admin form = CommonUtils.map2Bean(req.getParameterMap(), Admin.class);
		Admin admin = adminService.login(form);
		if(admin == null){
			req.setAttribute("msg", "用户名或密码错误!");
			return "f:/adminjsps/login.jsp";
		}
		req.getSession().setAttribute("admin", admin);
		return "r:/adminjsps/admin/main.jsp";
	}
}
