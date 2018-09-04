package cn.cdut.bookcity.user.domain;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpSession;

/**
 * �û��޸������
 * @author huangyong
 * @date 2018��4��24�� ����10:19:21
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
		
		//У��������
		if(newpass == null || "" == newpass.trim()){
			errors.put("newpass", "���벻��Ϊ��!");
		}else if(newpass.length() < 5 || newpass.length() > 20){
			errors.put("newpass", "��������5~20���ַ�֮��!");
		}
		
		//У��ȷ��������
		if(reloginpass == null || "" == reloginpass.trim()){
			errors.put("reloginpass", "ȷ�����벻��Ϊ��!");
		}else if(! reloginpass.equals(newpass)){
			errors.put("reloginpass", "�������벻ͬ!");
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
