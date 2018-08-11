package com.edu.util;

import java.io.UnsupportedEncodingException;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.Authenticator;
import javax.mail.Message.RecipientType;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.internet.MimeUtility;

public class EmailUtil {
	static String str = "<p>" + "	<br />"
			+ "	<p style=\"font-family:&quot;Microsoft YaHei&quot;;white-space:normal;font-size:medium;\">"
			+ "		<span style=\"font-size:14px;\">黄部长</span>" + "	</p>"
			+ "	<p style=\"font-family:&quot;Microsoft YaHei&quot;;white-space:normal;font-size:14px;\">" + "		中午好"
			+ "	</p>" + "	<p style=\"font-family:&quot;Microsoft YaHei&quot;;white-space:normal;font-size:14px;\">"
			+ "		附件是2018年8月日报" + "	</p>"
			+ "	<p style=\"font-family:&quot;Microsoft YaHei&quot;;white-space:normal;font-size:14px;\">"
			+ "		文章石墨连接" + "	</p>"
			+ "	<p style=\"font-family:&quot;Microsoft YaHei&quot;;white-space:normal;font-size:medium;\">"
			+ "		https://shimo.im/docs/VoKNDEh28CoRWeaA/ 《2018年8月开发组黄官易日报》，可复制链接后用石墨文档 App 打开" + "	</p>"
			+ "	<p style=\"font-family:&quot;Microsoft YaHei&quot;;white-space:normal;font-size:medium;\">"
			+ "		<span style=\"color:#2B2B2B;font-size:14px;\">PDF版本见附件，请您查阅</span>" + "	</p>" + "</p>"
			+ "<div id=\"signature\">" + "</div>";

	public static void initSession() {
		// 配置对象
		Properties props = new Properties();
		// 邮件服务器
		props.put("mail.host", "mail.x.xinhai.com");
		// 外网强制验证
		props.setProperty("mail.smtp.auth", "true");
		// 创建session对象
		Session session = Session.getInstance(props, new Authenticator() {
			@Override
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication("hgy@x.xinhai.com", "Xinhai0574");
			}
		});

		MimeMessage message = new MimeMessage(session);
		try {
			// 设置发件人信息
			message.setFrom(new InternetAddress("hgy@x.xinhai.com"));
			// 设置收件人
			message.addRecipient(RecipientType.TO, new InternetAddress("ytianzhe@x.xinhai.com"));
			// 设置主体
			message.setSubject("超级好用的偷懒邮件工具");
			// 设置内容
			MimeMultipart multipart = new MimeMultipart("mixed");
			message.setContent(multipart);
			MimeBodyPart content = new MimeBodyPart();
			MimeBodyPart attch1 = new MimeBodyPart();

			multipart.addBodyPart(content);
			multipart.addBodyPart(attch1);

			DataSource ds1 = new FileDataSource("E:\\pdfSender\\2018年8月开发组黄官易日报.pdf");
			DataHandler dh1 = new DataHandler(ds1);
			attch1.setDataHandler(dh1);
			attch1.setFileName(MimeUtility.encodeText("2018年8月开发组黄官易日报"));

			MimeMultipart bodyMultipart = new MimeMultipart("related");
			content.setContent(bodyMultipart);
			// 构造正文
			MimeBodyPart htmlBody = new MimeBodyPart();
			bodyMultipart.addBodyPart(htmlBody);
			htmlBody.setContent(str, "text/html;charset=UTF-8");

			message.saveChanges();
			// Transport对象
			Transport.send(message);
		} catch (AddressException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (MessagingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void main(String[] arg) {
		EmailUtil.initSession();
	}

}
