package cn.cdut.bookcity.user.web.servlet;

import java.io.IOException;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import cn.cdut.bookcity.user.domain.LoginForm;
import cn.cdut.bookcity.user.domain.RegistForm;
import cn.cdut.bookcity.user.domain.UpdatePasswordForm;
import cn.cdut.bookcity.user.domain.UserBean;
import cn.cdut.bookcity.user.exception.RepeatActivationException;
import cn.cdut.bookcity.user.exception.UserNotActivatedException;
import cn.cdut.bookcity.user.exception.UserNotExistException;
import cn.cdut.bookcity.user.service.UserServiceImpl;
import cn.cdut.servlet.BaseServlet;
import cn.cdut.util.common.CommonUtils;

/**
 * 处理用户模块的登陆、注册等用户相关功能的servlet
 * @author huangyong
 * @date 2018年4月19日 上午11:28:56
 */
@SuppressWarnings("serial")
public class UserServlet extends BaseServlet {
	private static UserServiceImpl userService = new UserServiceImpl();
	
	/**
	 * 处理用户注册功能的方法
	 * @return String   返回类型
	 */
	@SuppressWarnings("rawtypes")
	public String regist(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException{
		try {
			Map form = request.getParameterMap();
			HttpSession session = request.getSession();
			RegistForm registForm = CommonUtils.map2Bean(form, RegistForm.class);
			//校验表单
			Map errors = registForm.validate(userService, session);
			if(errors.size() > 0){
				request.setAttribute("registForm", registForm);
				request.setAttribute("errors", errors);
				//转发到/jsps/user/regist.jsp
				return "f:/jsps/user/regist.jsp";
			}
			
			//调用服务层regist方法注册用户
			UserBean userBean = new UserBean();
			CommonUtils.copyBean(registForm, userBean);
			userService.regist(userBean);
		}catch (Exception e) {
			throw new RuntimeException(e);
		}
		request.setAttribute("code", "success");
		request.setAttribute("msg", "恭喜!注册成功!请马上到邮箱激活帐号!");
		return "f:/jsps/msg.jsp";
	}
	
	/**
	 * 用户登陆
	 * @param request
	 * @param response
	 * @return String   返回类型
	 */
	public String login(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException{
		LoginForm loginForm = CommonUtils.map2Bean(request.getParameterMap(), LoginForm.class);
		Map<String, String> errors = loginForm.validate(request.getSession());
		if(! errors.isEmpty()){
			request.setAttribute("errors", errors);
			request.setAttribute("loginForm", loginForm);
			return "f:/jsps/user/login.jsp";
		}
		try {
			//下一行方法会抛出UserNotExistException、UserNotActivatedException异常
			UserBean user = userService.login(loginForm.getLoginname(), loginForm.getLoginpass());
			request.getSession().setAttribute("user", user);
			//设置cookie,使用户下次登陆时显示登录名
			Cookie cookie = new Cookie("loginname", user.getLoginname());
			//设置客户端5天的保存时间
			cookie.setMaxAge(60 * 60 * 24 * 5);
			response.addCookie(cookie);
			return "r:/jsps/main.jsp";
		} catch (UserNotExistException e) {
			request.setAttribute("msg", "用户名或密码错误!");
		} catch (UserNotActivatedException e) {
			request.setAttribute("msg", "用户未被激活!");
		}
		request.setAttribute("loginForm", loginForm);
		return "f:/jsps/user/login.jsp";
	}
	
	/**
	 * 用户退出 
	 * @param request
	 * @param response
	 * @throws ServletException 
	 * @return String   返回类型
	 */
	public String loginout(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException{
		//直接销毁相关session
		request.getSession().invalidate();
		return "r:/jsps/main.jsp";
	}
	
	/**
	 * 用户修改密码 
	 * @return String   返回类型
	 */
	public String updatePassword(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException{
		UpdatePasswordForm form = CommonUtils.map2Bean(request.getParameterMap(), UpdatePasswordForm.class);
		HttpSession session = request.getSession();
		Map<String, String> errors = form.validate(session);
		//若存在校验失败信息,则将错误信息转发回去
		if(! errors.isEmpty()){
			request.setAttribute("errors", errors);
			return	"/jsps/user/pwd.jsp";
		}
		UserBean loginUser = (UserBean) session.getAttribute("user");
		
		//若没登陆,转发到登陆界面
		if(loginUser == null){
			request.setAttribute("msg", "你没登陆!");
			return "/jsps/user/login.jsp";
		}
		try {
			//若抛出UserNotExistException异常则表示填写的原密码不对
			userService.updatePassword(loginUser.getUid(), form.getLoginpass(), form.getNewpass());
		} catch (UserNotExistException e) {
			request.setAttribute("msg", "原密码不正确!");
			return "f:/jsps/user/pwd.jsp";
		}
		request.setAttribute("code", "success");
		request.setAttribute("msg", "恭喜!密码修改成功!");
		return "f:/jsps/msg.jsp";
	}
	
	/**
	 * 激活注册用户
	 * @param request
	 * @param response
	 * @throws ServletException 
	 * @return String   返回类型
	 */
	public String activation(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException{
		String activationCode = request.getParameter("activationCode");
		try {
			userService.activation(activationCode);
			request.setAttribute("msg", "激活成功!请登录!");
			request.setAttribute("code", "success");
		} catch (UserNotExistException e) {
			request.setAttribute("msg", "无效激活码!");
			request.setAttribute("code", "error");
		} catch (RepeatActivationException e) {
			request.setAttribute("msg", "请不要重复激活!");
			request.setAttribute("code", "error");
		}
		return "f:/jsps/msg.jsp";
	}
	
	/**
	 * ajax请求校验用户名是否存在
	 * @param request
	 * @param response
	 * @throws IOException 
	 * @return String   返回类型
	 */
	public String validateLoginname(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException,IOException{
		boolean isExist = false;
		String loginname = request.getParameter("loginname");
		if(loginname != null && loginname.trim() != ""){
			isExist = userService.ajaxValidateLoginname(loginname);
		}
		response.getWriter().print(isExist);
		return null;
	}
	
	/**
	 * ajax请求校验密码是否正确 
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException 
	 * @return String   返回类型
	 */
	public String validateLoginpass(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException,IOException{
		boolean isTrue = false;
		String loginpass = request.getParameter("loginpass");
		UserBean loginUser = (UserBean) request.getSession().getAttribute("user");
		if(loginUser != null){
			if(loginpass != null && loginpass.trim() != ""){
				isTrue = userService.ajaxValidateLoginpass(loginUser.getUid(), loginpass);
			}
		}
		response.getWriter().print(isTrue);
		return null;
	}
	
	/**
	 * ajax请求校验email地址是否存在
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException 
	 * @return String   返回类型
	 */
	public String validateEmail(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException,IOException{
		boolean isExist = false;
		String email = request.getParameter("email");
		if(email != null && email.trim() != ""){
			isExist = userService.ajaxValidateEmail(email);
		}
		response.getWriter().print(isExist);
		return null;
	}
	
	/**
	 * ajax请求验证session中的vcode是否与表单的验证码相同
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException 
	 * @return String   返回类型
	 */
	public String validateVerifyCode(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException{
		String verifyCode = request.getParameter("verifyCode");
		HttpSession session = request.getSession(false);
		if(session != null){
			String vcode = (String) session.getAttribute("vcode");
			if(verifyCode !=null && verifyCode.equalsIgnoreCase(vcode)){
				response.getWriter().print(true);
				return null;
			}
		}
		response.getWriter().print(false);
		return null;
	}
}
