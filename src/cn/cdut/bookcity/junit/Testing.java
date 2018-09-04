package cn.cdut.bookcity.junit;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.text.MessageFormat;
import java.util.Map;
import java.util.Properties;

import javax.mail.Session;
import javax.mail.internet.InternetAddress;

import org.apache.commons.dbutils.handlers.MapHandler;
import org.junit.Test;

import cn.cdut.bookcity.cart.domain.CartItem;
import cn.cdut.util.common.CommonUtils;
import cn.cdut.util.jdbc.TxQueryRunner;
import cn.cdut.util.mail.Mail;
import cn.cdut.util.mail.MailUtils;

public class Testing {
	@Test
	public void testSendMail() {
		try {
			// 校验通过发送激活邮件
			Properties pro = new Properties();
			InputStream inStream = this.getClass().getClassLoader()
					.getResourceAsStream("email_template.properties");
			pro.load(inStream);

			String from = pro.getProperty("from");
			String fromName = pro.getProperty("fromName");
			String subject = pro.getProperty("subject");
			// 获取内容模板，替换其中的激活码
			String activationCode = CommonUtils.uuid() + CommonUtils.uuid();
			String content = MessageFormat.format(pro.getProperty("content"),
					activationCode);
			String host = pro.getProperty("host");
			String username = pro.getProperty("username");
			String password = pro.getProperty("password");
			System.out
					.println(from + "\n" + fromName + "\n" + subject + "\n"
							+ content + "\n" + host + "\n" + username + "\n"
							+ password);

			InternetAddress fromAddress = MailUtils.createAddress(from,
					fromName, "utf-8");
			InternetAddress toAddress = MailUtils.createAddress(
					"756046720@qq.com", null, "utf-8");
			// 创建邮件对象
			Mail mail = new Mail(fromAddress, toAddress, subject, content);
			// 获得邮件服务器会话
			Session mailSession = MailUtils.createSession(host, username,
					password);
			// 发送邮件
			MailUtils.send(mailSession, mail);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void findByBookAndUser() throws SQLException{
		TxQueryRunner qr = new TxQueryRunner();
		String sql = "SELECT * FROM t_cartitem WHERE bid=? AND uid=?";
		Map<String, Object> map = qr.query(sql, new MapHandler(), "1", "1");
		System.out.println(map);
	}
	
	@Test
	public void testFile() throws URISyntaxException, IOException {
		URI uri = new URI("file:/E:/Eclipse/servers/apache-tomcat-8.0.48/webapps/bookcity/book_img/");
		File image =new File(new File(uri), "test.jpg");
		System.out.println(image.exists());
	}
}
