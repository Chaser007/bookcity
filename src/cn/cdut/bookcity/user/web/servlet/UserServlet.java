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
 * �����û�ģ��ĵ�½��ע����û���ع��ܵ�servlet
 * @author huangyong
 * @date 2018��4��19�� ����11:28:56
 */
@SuppressWarnings("serial")
public class UserServlet extends BaseServlet {
	private static UserServiceImpl userService = new UserServiceImpl();
	
	/**
	 * �����û�ע�Ṧ�ܵķ���
	 * @return String   ��������
	 */
	@SuppressWarnings("rawtypes")
	public String regist(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException{
		try {
			Map form = request.getParameterMap();
			HttpSession session = request.getSession();
			RegistForm registForm = CommonUtils.map2Bean(form, RegistForm.class);
			//У���
			Map errors = registForm.validate(userService, session);
			if(errors.size() > 0){
				request.setAttribute("registForm", registForm);
				request.setAttribute("errors", errors);
				//ת����/jsps/user/regist.jsp
				return "f:/jsps/user/regist.jsp";
			}
			
			//���÷����regist����ע���û�
			UserBean userBean = new UserBean();
			CommonUtils.copyBean(registForm, userBean);
			userService.regist(userBean);
		}catch (Exception e) {
			throw new RuntimeException(e);
		}
		request.setAttribute("code", "success");
		request.setAttribute("msg", "��ϲ!ע��ɹ�!�����ϵ����伤���ʺ�!");
		return "f:/jsps/msg.jsp";
	}
	
	/**
	 * �û���½
	 * @param request
	 * @param response
	 * @return String   ��������
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
			//��һ�з������׳�UserNotExistException��UserNotActivatedException�쳣
			UserBean user = userService.login(loginForm.getLoginname(), loginForm.getLoginpass());
			request.getSession().setAttribute("user", user);
			//����cookie,ʹ�û��´ε�½ʱ��ʾ��¼��
			Cookie cookie = new Cookie("loginname", user.getLoginname());
			//���ÿͻ���5��ı���ʱ��
			cookie.setMaxAge(60 * 60 * 24 * 5);
			response.addCookie(cookie);
			return "r:/jsps/main.jsp";
		} catch (UserNotExistException e) {
			request.setAttribute("msg", "�û������������!");
		} catch (UserNotActivatedException e) {
			request.setAttribute("msg", "�û�δ������!");
		}
		request.setAttribute("loginForm", loginForm);
		return "f:/jsps/user/login.jsp";
	}
	
	/**
	 * �û��˳� 
	 * @param request
	 * @param response
	 * @throws ServletException 
	 * @return String   ��������
	 */
	public String loginout(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException{
		//ֱ���������session
		request.getSession().invalidate();
		return "r:/jsps/main.jsp";
	}
	
	/**
	 * �û��޸����� 
	 * @return String   ��������
	 */
	public String updatePassword(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException{
		UpdatePasswordForm form = CommonUtils.map2Bean(request.getParameterMap(), UpdatePasswordForm.class);
		HttpSession session = request.getSession();
		Map<String, String> errors = form.validate(session);
		//������У��ʧ����Ϣ,�򽫴�����Ϣת����ȥ
		if(! errors.isEmpty()){
			request.setAttribute("errors", errors);
			return	"/jsps/user/pwd.jsp";
		}
		UserBean loginUser = (UserBean) session.getAttribute("user");
		
		//��û��½,ת������½����
		if(loginUser == null){
			request.setAttribute("msg", "��û��½!");
			return "/jsps/user/login.jsp";
		}
		try {
			//���׳�UserNotExistException�쳣���ʾ��д��ԭ���벻��
			userService.updatePassword(loginUser.getUid(), form.getLoginpass(), form.getNewpass());
		} catch (UserNotExistException e) {
			request.setAttribute("msg", "ԭ���벻��ȷ!");
			return "f:/jsps/user/pwd.jsp";
		}
		request.setAttribute("code", "success");
		request.setAttribute("msg", "��ϲ!�����޸ĳɹ�!");
		return "f:/jsps/msg.jsp";
	}
	
	/**
	 * ����ע���û�
	 * @param request
	 * @param response
	 * @throws ServletException 
	 * @return String   ��������
	 */
	public String activation(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException{
		String activationCode = request.getParameter("activationCode");
		try {
			userService.activation(activationCode);
			request.setAttribute("msg", "����ɹ�!���¼!");
			request.setAttribute("code", "success");
		} catch (UserNotExistException e) {
			request.setAttribute("msg", "��Ч������!");
			request.setAttribute("code", "error");
		} catch (RepeatActivationException e) {
			request.setAttribute("msg", "�벻Ҫ�ظ�����!");
			request.setAttribute("code", "error");
		}
		return "f:/jsps/msg.jsp";
	}
	
	/**
	 * ajax����У���û����Ƿ����
	 * @param request
	 * @param response
	 * @throws IOException 
	 * @return String   ��������
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
	 * ajax����У�������Ƿ���ȷ 
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException 
	 * @return String   ��������
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
	 * ajax����У��email��ַ�Ƿ����
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException 
	 * @return String   ��������
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
	 * ajax������֤session�е�vcode�Ƿ��������֤����ͬ
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException 
	 * @return String   ��������
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
