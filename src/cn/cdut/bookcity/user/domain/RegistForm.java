package cn.cdut.bookcity.user.domain;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpSession;

import cn.cdut.bookcity.user.service.UserServiceImpl;

/**
 * 用户注册表单实体
 * @author huangyong
 * @date 2018年4月19日 下午3:07:07
 */
public class RegistForm {

	private String loginname;
	private String loginpass;
	private String reloginpass;
	private String email;
	private String verifyCode;
	
	public String getLoginname() {
		return loginname;
	}
	public void setLoginname(String loginname) {
		this.loginname = loginname;
	}
	public String getLoginpass() {
		return loginpass;
	}
	public void setLoginpass(String loginpass) {
		this.loginpass = loginpass;
	}
	public String getReloginpass() {
		return reloginpass;
	}
	public void setReloginpass(String reloginpass) {
		this.reloginpass = reloginpass;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getVerifyCode() {
		return verifyCode;
	}
	public void setVerifyCode(String verifyCode) {
		this.verifyCode = verifyCode;
	}
	
	/**
	 * 校验注册表单方法
	 * @return boolean   返回类型
	 */
	public Map<String, String> validate(UserServiceImpl userService, HttpSession session){
		Map<String,String> errors = new HashMap<String, String>();
		
		//校验用户名
		if(loginname == null || "" == loginname.trim()){
			errors.put("loginname", "用户名不能为空!");
		}else if(loginname.length() < 3 || loginname.length() > 20){
			errors.put("loginname", "用户名需在3~20个字符之间!");
		}else if(! loginname.matches("^[a-zA-Z]+[\\w_]*$")){
			errors.put("loginname", "可使用字母、数字、下划线，需以字母开头!");
		}else if(userService.ajaxValidateLoginname(loginname)){
			errors.put("loginname", "该用户名已被注册!");
		}
		
		//校验密码
		if(loginpass == null || "" == loginpass.trim()){
			errors.put("loginpass", "密码不能为空!");
		}else if(loginpass.length() < 5 || loginpass.length() > 20){
			errors.put("loginpass", "密码需在5~20个字符之间!");
		}
		
		//校验重新输入密码
		if(reloginpass == null || "" == reloginpass.trim()){
			errors.put("reloginpass", "确认密码不能为空!");
		}else if(! reloginpass.equals(loginpass)){
			errors.put("reloginpass", "两次密码不同!");
		}
		
		//校验邮箱地址
		if(email == null || "" == email.trim()){
			errors.put("email", "邮箱不能为空!");
		}else if(! email.matches("^[\\w-_]+@[\\w-_]+(\\.[\\w-_]+)+$")){
			errors.put("email", "邮箱格式不正确!");
		}else if(userService.ajaxValidateEmail(email)){
			errors.put("email", "该邮箱已被注册!");
		}
		
		//校验验证码
		if(verifyCode == null || "" == verifyCode.trim()){
			errors.put("verifyCode", "验证码不能为空!");
		}else if(! verifyCode.equalsIgnoreCase((String) session.getAttribute("vcode"))){
			errors.put("verifyCode", "验证码错误!");
		}
		
		return errors;
	}
	
}
