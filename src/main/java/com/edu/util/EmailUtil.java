package com.edu.util;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EmailUtil {
	private static Logger log = LoggerFactory.getLogger(EmailUtil.class);
	private static String str = "<p>" + "	<br />"
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

	public static void sendEmail() {
		Properties properties = IOUtil.loadPropertyFile("/email.properties");
		// 配置对象
		Properties props = new Properties();
		// 邮件服务器
		props.put("mail.host", properties.getProperty("email.host"));
		// 外网强制验证
		props.setProperty("mail.smtp.auth", "true");
		// 创建session对象Authenticator
		Authenticator authenticator = new Authenticator() {
			@Override
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(properties.getProperty("email.Account"),
						properties.getProperty("email.Password"));
			}
		};
		Session session = Session.getInstance(props, authenticator);
		try {
			String[] fileArr = getAttachments();
			if (null == fileArr) {
				return;
			}
			MimeMessage message = new MimeMessage(session);
			// 设置发件人信息
			message.setFrom(new InternetAddress(properties.getProperty("email.From")));
			// 设置收件人
			message.addRecipient(RecipientType.TO, new InternetAddress(properties.getProperty("email.Recipient")));
			// 设置主体
			message.setSubject(fileArr[1]);
			// 设置内容
			// multipart: mixed、alternative、related
			MimeMultipart multipart = new MimeMultipart("mixed");
			message.setContent(multipart);
			MimeBodyPart content = new MimeBodyPart();
			multipart.addBodyPart(content);

			MimeBodyPart attch1 = new MimeBodyPart();
			multipart.addBodyPart(attch1);

			DataSource ds1 = new FileDataSource(fileArr[0]);
			DataHandler dh1 = new DataHandler(ds1);
			attch1.setDataHandler(dh1);
			attch1.setFileName(MimeUtility.encodeText(fileArr[1]));
			// }
			// 构造正文
			MimeMultipart bodyMultipart = new MimeMultipart("alternative");
			content.setContent(bodyMultipart);
			MimeBodyPart htmlBody = new MimeBodyPart();
			bodyMultipart.addBodyPart(htmlBody);
			htmlBody.setContent(str, "text/html;charset=UTF-8");
			message.saveChanges();
			// Transport对象
			Transport.send(message);
		} catch (

		AddressException e) {
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
		EmailUtil.sendEmail();
	}

	public static String[] getAttachments() {
		String[] fileArr = new String[2];
		String path = EmailUtil.class.getResource("/").getPath();
		try {
			fileArr[0] = URLDecoder.decode(path + "../../upload/2018年" + DateUtil.getMonthNum() + "月"
					+ DateUtil.getDayNumWithMonth() + "日开发组黄官易日报.pdf", "utf-8");
			System.err.println(fileArr[0]);
			File file = new File(fileArr[0]);
			
			if (file.exists()) {
				fileArr[1] = file.getName();
				return fileArr;
			}
		} catch (UnsupportedEncodingException e) {
			log.error("未上传当天的日报 请及时上传");
		}
		return null;
	}
}
