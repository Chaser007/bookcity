package cn.cdut.bookcity.user.domain;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpSession;

/**
 * �û���½��
 * @author huangyong
 * @date 2018��4��22�� ����11:01:43
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
	 * ��½��У�鷽��
	 * @param session
	 * @return Map<String,String>   ��������
	 */
	public Map<String,String> validate(HttpSession session){
		Map<String,String> errors = new HashMap<String, String>();
		
		//У���û���
		if(loginname == null || "" == loginname.trim()){
			errors.put("loginname", "�û�������Ϊ��!");
		}else if(loginname.length() < 3 || loginname.length() > 20){
			errors.put("loginname", "�û�������3~20���ַ�֮��!");
		}
		
		//У������
		if(loginpass == null || "" == loginpass.trim()){
			errors.put("loginpass", "���벻��Ϊ��!");
		}else if(loginpass.length() < 5 || loginpass.length() > 20){
			errors.put("loginpass", "��������5~20���ַ�֮��!");
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
