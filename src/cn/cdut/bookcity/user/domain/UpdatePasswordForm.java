package cn.cdut.bookcity.user.domain;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpSession;

/**
 * 用户修改密码表单
 * @author huangyong
 * @date 2018年4月24日 下午10:19:21
 */
public class UpdatePasswordForm {
	private String loginpass;
	private String newpass;
	private String reloginpass;
	private String verifyCode;

	public String getLoginpass() {
		return loginpass;
	}
	public void setLoginpass(String loginpass) {
		this.loginpass = loginpass;
	}

	public String getNewpass() {
		return newpass;
	}
	public void setNewpass(String newpass) {
		this.newpass = newpass;
	}

	public String getReloginpass() {
		return reloginpass;
	}
	public void setReloginpass(String reloginpass) {
		this.reloginpass = reloginpass;
	}

	public String getVerifyCode() {
		return verifyCode;
	}
	public void setVerifyCode(String verifyCode) {
		this.verifyCode = verifyCode;
	}

	public Map<String,String> validate(HttpSession session){
		Map<String,String> errors = new HashMap<String, String>();
		
		//校验新密码
		if(newpass == null || "" == newpass.trim()){
			errors.put("newpass", "密码不能为空!");
		}else if(newpass.length() < 5 || newpass.length() > 20){
			errors.put("newpass", "密码需在5~20个字符之间!");
		}
		
		//校验确认新密码
		if(reloginpass == null || "" == reloginpass.trim()){
			errors.put("reloginpass", "确认密码不能为空!");
		}else if(! reloginpass.equals(newpass)){
			errors.put("reloginpass", "两次密码不同!");
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
