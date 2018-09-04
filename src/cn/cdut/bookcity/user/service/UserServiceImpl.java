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
 * 用户模块的服务类
 * @author huangyong
 * @date 2018年4月19日 下午6:46:15
 */
public class UserServiceImpl {
	private static UserDaoImpl userDao = new UserDaoImpl();
	
	/**
	 * 注册添加用户
	 * @param user 
	 * @return void   返回类型
	 */
	public void regist(UserBean user){
		try {
			//校验通过发送激活邮件
			Properties pro = new Properties();
			InputStream inStream = this.getClass().getClassLoader().getResourceAsStream("email_template.properties");
			pro.load(inStream);
			
			String from = pro.getProperty("from");
			String fromName = pro.getProperty("fromName");
			String subject = pro.getProperty("subject");
			//获取内容模板，替换其中的激活码
			String activationCode = CommonUtils.uuid() + CommonUtils.uuid();
			String content = MessageFormat.format(pro.getProperty("content"),activationCode);
			String host = pro.getProperty("host");
			String username = pro.getProperty("username");
			String password = pro.getProperty("password");
			
			InternetAddress fromAddress = MailUtils.createAddress(from, fromName, "utf-8");
			InternetAddress toAddress = MailUtils.createAddress(user.getEmail(), user.getLoginname(), "utf-8");
			//创建邮件对象
			Mail mail = new Mail(fromAddress, toAddress, subject, content);
			//获得邮件服务器会话
			Session mailSession = MailUtils.createSession(host, username, password);
			//发送邮件
			MailUtils.send(mailSession, mail);
			
			//在数据库中添加用户UserBean
			user.setUid(CommonUtils.uuid());
			user.setStatus(false);
			user.setActivationCode(activationCode);
			userDao.addUser(user);
		} catch (Exception e) {
			throw new RuntimeException("用户注册失败!");
		}
	}
	
	/**
	 * 用户登陆服务
	 * @param loginname
	 * @param loginpass
	 * @return UserBean   返回类型
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
	 * 用户修改密码服务
	 * @param uid
	 * @param password 
	 * @return void   返回类型
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
	 * 激活用户服务
	 * @param code 
	 * @return void   返回类型
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
	 * 校验指定的用户名是否存在
	 * @param loginname
	 * @return boolean   返回类型
	 */
	public boolean ajaxValidateLoginname(String loginname){
		try {
			return userDao.ajaxValidateLoginname(loginname);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * 校验对应uid的密码是否正确 
	 * @param uid
	 * @param loginpass
	 * @return boolean   返回类型
	 */
	public boolean ajaxValidateLoginpass(String uid, String loginpass) {
		try {
			return userDao.findByUidAndPass(uid, loginpass);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * 校验邮箱地址是否已经存在
	 * @param email
	 * @return boolean   返回类型
	 */
	public boolean ajaxValidateEmail(String email){
		try {
			return userDao.ajaxValidateEmail(email);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
}
