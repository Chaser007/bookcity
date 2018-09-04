package cn.cdut.bookcity.user.domain;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpSession;

/**
 * 用户登陆表单
 * @author huangyong
 * @date 2018年4月22日 上午11:01:43
 */
public class LoginForm {
	private String loginname;
	private String loginpass;
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
	public String getVerifyCode() {
		return verifyCode;
	}
	public void setVerifyCode(String verifyCode) {
		this.verifyCode = verifyCode;
	}
	
	/**
	 * 登陆表单校验方法
	 * @param session
	 * @return Map<String,String>   返回类型
	 */
	public Map<String,String> validate(HttpSession session){
		Map<String,String> errors = new HashMap<String, String>();
		
		//校验用户名
		if(loginname == null || "" == loginname.trim()){
			errors.put("loginname", "用户名不能为空!");
		}else if(loginname.length() < 3 || loginname.length() > 20){
			errors.put("loginname", "用户名需在3~20个字符之间!");
		}
		
		//校验密码
		if(loginpass == null || "" == loginpass.trim()){
			errors.put("loginpass", "密码不能为空!");
		}else if(loginpass.length() < 5 || loginpass.length() > 20){
			errors.put("loginpass", "密码需在5~20个字符之间!");
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
