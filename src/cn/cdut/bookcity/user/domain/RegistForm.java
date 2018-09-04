package cn.cdut.bookcity.user.domain;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpSession;

import cn.cdut.bookcity.user.service.UserServiceImpl;

/**
 * �û�ע���ʵ��
 * @author huangyong
 * @date 2018��4��19�� ����3:07:07
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
	 * У��ע�������
	 * @return boolean   ��������
	 */
	public Map<String, String> validate(UserServiceImpl userService, HttpSession session){
		Map<String,String> errors = new HashMap<String, String>();
		
		//У���û���
		if(loginname == null || "" == loginname.trim()){
			errors.put("loginname", "�û�������Ϊ��!");
		}else if(loginname.length() < 3 || loginname.length() > 20){
			errors.put("loginname", "�û�������3~20���ַ�֮��!");
		}else if(! loginname.matches("^[a-zA-Z]+[\\w_]*$")){
			errors.put("loginname", "��ʹ����ĸ�����֡��»��ߣ�������ĸ��ͷ!");
		}else if(userService.ajaxValidateLoginname(loginname)){
			errors.put("loginname", "���û����ѱ�ע��!");
		}
		
		//У������
		if(loginpass == null || "" == loginpass.trim()){
			errors.put("loginpass", "���벻��Ϊ��!");
		}else if(loginpass.length() < 5 || loginpass.length() > 20){
			errors.put("loginpass", "��������5~20���ַ�֮��!");
		}
		
		//У��������������
		if(reloginpass == null || "" == reloginpass.trim()){
			errors.put("reloginpass", "ȷ�����벻��Ϊ��!");
		}else if(! reloginpass.equals(loginpass)){
			errors.put("reloginpass", "�������벻ͬ!");
		}
		
		//У�������ַ
		if(email == null || "" == email.trim()){
			errors.put("email", "���䲻��Ϊ��!");
		}else if(! email.matches("^[\\w-_]+@[\\w-_]+(\\.[\\w-_]+)+$")){
			errors.put("email", "�����ʽ����ȷ!");
		}else if(userService.ajaxValidateEmail(email)){
			errors.put("email", "�������ѱ�ע��!");
		}
		
		//У����֤��
		if(verifyCode == null || "" == verifyCode.trim()){
			errors.put("verifyCode", "��֤�벻��Ϊ��!");
		}else if(! verifyCode.equalsIgnoreCase((String) session.getAttribute("vcode"))){
			errors.put("verifyCode", "��֤�����!");
		}
		
		return errors;
	}
	
}
