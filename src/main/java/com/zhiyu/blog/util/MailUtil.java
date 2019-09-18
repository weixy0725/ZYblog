package com.zhiyu.blog.util;

import java.io.UnsupportedEncodingException;
import java.security.Security;
import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.apache.commons.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Value;

public class MailUtil {

	private static final String ALIDM_SMTP_HOST = "smtp.126.com";
	private static final String ALIDM_SMTP_PORT = "465";// 阿里云把25禁用了，只能使用ssl465端口了
	private static final String MAIL_USER="";
	private static final String MAIL_PASSWORD="";

	@SuppressWarnings("restriction")
	public static void sendMail(Integer type,String sendTo, String message) throws UnsupportedEncodingException, MessagingException {
		// 配置发送邮件的环境属性
		Security.addProvider(new com.sun.net.ssl.internal.ssl.Provider());  
        final String SSL_FACTORY = "javax.net.ssl.SSLSocketFactory";  
		final Properties props = new Properties();
		// 表示SMTP发送邮件，需要进行身份验证
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.host", ALIDM_SMTP_HOST); 
		props.put("mail.smtp.port", ALIDM_SMTP_PORT);
		props.setProperty("mail.smtp.socketFactory.class", SSL_FACTORY);
        props.setProperty("mail.smtp.socketFactory.fallback", "false");
		// 发件人的账号，填写控制台配置的发信地址,比如xxx@xxx.com
		props.put("mail.user", MAIL_USER);
		// 访问SMTP服务时需要提供的密码(在控制台选择发信地址进行设置)
		props.put("mail.password",MAIL_PASSWORD);
		
		 // 构建授权信息，用于进行SMTP进行身份验证
        Authenticator authenticator = new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                // 用户名、密码
                String userName = props.getProperty("mail.user");
                String password = props.getProperty("mail.password");
                return new PasswordAuthentication(userName, password);
            }
        };
        
        // 使用环境属性和授权信息，创建邮件会话
        Session mailSession = Session.getInstance(props, authenticator);
        
        // 创建邮件消息
        MimeMessage buildMessage = new MimeMessage(mailSession);
        
        InternetAddress from = new InternetAddress(MAIL_USER, "Ayuan");
        
        buildMessage.setFrom(from);
        
        // 设置收件人邮件地址，比如yyy@yyy.com
        InternetAddress to = new InternetAddress(sendTo);
        buildMessage.setRecipient(MimeMessage.RecipientType.TO, to);
        
        if(type==0) {
        	// 设置邮件标题
        	buildMessage.setSubject("Ayuan既往随叙-留言回复通知邮件");
        	// 设置邮件的内容体
        	buildMessage.setContent("您的留言已经得到回复，回复内容："+message+"[当前内容由系统自动发送，请勿回复！]", "text/html;charset=UTF-8");
        }else {
        	buildMessage.setSubject("Ayuan既往随叙-网站新增留言通知");
        	buildMessage.setContent("网站有新留言了，快点登录后台查看一下吧~(*╹▽╹*)，留言内容："+message, "text/html;charset=UTF-8");
        }
        
        // 发送邮件
        Transport.send(buildMessage);

	}

}
