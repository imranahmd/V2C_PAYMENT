package com.rew.payment.utility;

import java.util.Properties;

import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class SendMailService
{
    private static Logger logger = LoggerFactory.getLogger(SendMailService.class);

	public SendMailService() {}

	public int sendMail(String to, String subject, String from, String mailBody, String cc) { 

		int status = 0;				
		try 
		{	
			//String host = "smtp.gmail.com";
			String host = PGUtils.getPropertyValue("smtpHost");
			String user = PGUtils.getPropertyValue("smtpUser");
			String pass = PGUtils.getPropertyValue("smtpPass");
			String port = PGUtils.getPropertyValue("smtpPort");
			String startTls = PGUtils.getPropertyValue("smtpStartTLS");
			String sslEnable = PGUtils.getPropertyValue("smtpSSLEnable");
			String smtpAuth = PGUtils.getPropertyValue("smtpAuth");

			// Get the session object
			Properties properties = System.getProperties();
			properties.setProperty("mail.smtp.host", host);
			properties.setProperty("mail.smtp.user", user);
			properties.setProperty("mail.smtp.password", pass);
			properties.put("mail.smtp.auth", smtpAuth);
			properties.put("mail.smtp.starttls.enable", startTls);
			properties.put("mail.smtp.ssl.enable", sslEnable);
			properties.put("mail.smtp.port", port);

			//Session session = Session.getDefaultInstance(properties);
			Session session = Session.getDefaultInstance(properties, 
					new javax.mail.Authenticator(){
				protected PasswordAuthentication getPasswordAuthentication() {
					return new PasswordAuthentication(
							user, pass);// Specify the Username and the PassWord
				}
			});



			logger.info("SendMailService.java ::: sendMail() :: Mail Params are : {"+to+","+from+","+subject+","+mailBody+"}");
			String[] recipientList = to.split(",");

			MimeMessage message = new MimeMessage(session);			
			message.setFrom(new InternetAddress(from));
			for (int i=0 ; i < recipientList.length ; i++)
			{
				logger.info("SendMailService.java ::: sendMail() :: Mail Recipients TO are : "+recipientList[i].toString());
				message.addRecipient(Message.RecipientType.TO, new InternetAddress(recipientList[i].toString()));
			}


			if (cc != null && !cc.equalsIgnoreCase(""))
			{
				String[] ccList = cc.split(",");

				for (int i = 0; i < ccList.length; i++)
				{
					logger.info("SendMailService.java ::: sendMail() :: Mail Recipients CC are : "+ ccList[i].toString());
					message.addRecipient(Message.RecipientType.CC, new InternetAddress(ccList[i].toString()));
				} 
			}

			message.setSubject(subject);
			message.setContent(mailBody, "text/html");

			// Send message
			Transport.send(message);
			status = 1; 

		}
		catch (Exception mex)
		{
			status = -2;
			logger.info("SendMailService.java ::: sendMail() :: catch block :: Error in mail sending and Status --> "+status);
		}
		return status;

	}

	/*public static void main(String[] args) {}*/
}
