package cn.cdut.bookcity.user.service;

import java.io.InputStream;
import java.sql.SQLException;
import java.text.MessageFormat;
import java.util.Properties;

import javax.mail.Session;
import javax.mail.internet.InternetAddress;

import cn.cdut.bookcity.user.dao.UserDaoImpl;
import cn.cdut.bookcity.user.domain.UserBean;
import cn.cdut.bookcity.user.exception.RepeatActivationException;
import cn.cdut.bookcity.user.exception.UserNotActivatedException;
import cn.cdut.bookcity.user.exception.UserNotExistException;
import cn.cdut.util.common.CommonUtils;
import cn.cdut.util.mail.Mail;
import cn.cdut.util.mail.MailUtils;

/**
 * �û�ģ��ķ�����
 * @author huangyong
 * @date 2018��4��19�� ����6:46:15
 */
public class UserServiceImpl {
	private static UserDaoImpl userDao = new UserDaoImpl();
	
	/**
	 * ע������û�
	 * @param user 
	 * @return void   ��������
	 */
	public void regist(UserBean user){
		try {
			//У��ͨ�����ͼ����ʼ�
			Properties pro = new Properties();
			InputStream inStream = this.getClass().getClassLoader().getResourceAsStream("email_template.properties");
			pro.load(inStream);
			
			String from = pro.getProperty("from");
			String fromName = pro.getProperty("fromName");
			String subject = pro.getProperty("subject");
			//��ȡ����ģ�壬�滻���еļ�����
			String activationCode = CommonUtils.uuid() + CommonUtils.uuid();
			String content = MessageFormat.format(pro.getProperty("content"),activationCode);
			String host = pro.getProperty("host");
			String username = pro.getProperty("username");
			String password = pro.getProperty("password");
			
			InternetAddress fromAddress = MailUtils.createAddress(from, fromName, "utf-8");
			InternetAddress toAddress = MailUtils.createAddress(user.getEmail(), user.getLoginname(), "utf-8");
			//�����ʼ�����
			Mail mail = new Mail(fromAddress, toAddress, subject, content);
			//����ʼ��������Ự
			Session mailSession = MailUtils.createSession(host, username, password);
			//�����ʼ�
			MailUtils.send(mailSession, mail);
			
			//�����ݿ�������û�UserBean
			user.setUid(CommonUtils.uuid());
			user.setStatus(false);
			user.setActivationCode(activationCode);
			userDao.addUser(user);
		} catch (Exception e) {
			throw new RuntimeException("�û�ע��ʧ��!");
		}
	}
	
	/**
	 * �û���½����
	 * @param loginname
	 * @param loginpass
	 * @return UserBean   ��������
	 * @throws UserNotExistException 
	 * @throws UserNotActivatedException 
	 */
	public UserBean login(String loginname, String loginpass) throws UserNotExistException, UserNotActivatedException{
		try {
			UserBean user = userDao.findByLoginnameAndLoginPass(loginname, loginpass);
			if(user == null){
				throw new UserNotExistException();
			}
			if(! user.isStatus()){
				throw new UserNotActivatedException();
			}
			return user;
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * �û��޸��������
	 * @param uid
	 * @param password 
	 * @return void   ��������
	 * @throws UserNotExistException 
	 */
	public void updatePassword(String uid, String oldpass, String newpass) throws UserNotExistException{
		try {
			boolean isExist = userDao.findByUidAndPass(uid, oldpass);
			if(! isExist){
				throw new UserNotExistException();
			}
			userDao.updatePassword(uid, newpass);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * �����û�����
	 * @param code 
	 * @return void   ��������
	 * @throws UserNotExistException 
	 * @throws RepeatActivationException 
	 */
	public void activation(String code) throws UserNotExistException, RepeatActivationException{
		try {
			UserBean user = userDao.findByActivationCode(code);
			if(user == null){
				throw new UserNotExistException();
			}
			if(user.isStatus()){
				throw new RepeatActivationException();
			}
			userDao.updateStatus(user.getUid(), true);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * У��ָ�����û����Ƿ����
	 * @param loginname
	 * @return boolean   ��������
	 */
	public boolean ajaxValidateLoginname(String loginname){
		try {
			return userDao.ajaxValidateLoginname(loginname);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * У���Ӧuid�������Ƿ���ȷ 
	 * @param uid
	 * @param loginpass
	 * @return boolean   ��������
	 */
	public boolean ajaxValidateLoginpass(String uid, String loginpass) {
		try {
			return userDao.findByUidAndPass(uid, loginpass);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * У�������ַ�Ƿ��Ѿ�����
	 * @param email
	 * @return boolean   ��������
	 */
	public boolean ajaxValidateEmail(String email){
		try {
			return userDao.ajaxValidateEmail(email);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
}
