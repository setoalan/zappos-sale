package com.setoalan.zappossale;

import java.util.Date;
import java.util.Properties;

import javax.activation.CommandMap;
import javax.activation.MailcapCommandMap;
import javax.mail.BodyPart;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import android.util.Log;


public class EmailSender extends javax.mail.Authenticator {
	
	public static final String TAG = "EmailSender";
	
	private String mUser;
	private String mPassword;
	
	private String[] mTo;
	private String mFrom;
	
	private String mPort;
	private String mSPort;
	
	private String mHost;
	
	private String mSubject;
	private String mBody;
	
	private boolean mAuth;
	
	private boolean mDebuggable;
	private Multipart mMultipart;
	
	public Void initalize() {		
		try {
			EmailSender m = new EmailSender("aseto@umich.edu", "==");
			
			String[] toArr = {"seto.alan@yahoo.com"};
			m.setTo(toArr);
			m.setFrom("aseto@umich.edu");
			m.setSubject("SALE ON YOUR ITEM");
			m.setBody("YOUR ITEM IS ON SALE");
			
			if(m.send()) { 
				Log.i(TAG, "Email was sent successfully.");
			} else { 
				Log.i(TAG, "Email was not sent.");
			}
		} catch (AddressException e) {
			e.printStackTrace();
		} catch (MessagingException e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	public EmailSender() {
		mHost = "smtp.gmail.com";
		mPort = "465";
		mSPort = "465";
		
		mUser = "";
		mPassword = "";
		mFrom = "";
		mSubject = "";
		mBody = "";
		
		mDebuggable = false;
		mAuth = true;
		
		mMultipart = new MimeMultipart();
		
	    MailcapCommandMap mc = (MailcapCommandMap) CommandMap.getDefaultCommandMap(); 
	    mc.addMailcap("text/html;; x-java-content-handler=com.sun.mail.handlers.text_html"); 
	    mc.addMailcap("text/xml;; x-java-content-handler=com.sun.mail.handlers.text_xml"); 
	    mc.addMailcap("text/plain;; x-java-content-handler=com.sun.mail.handlers.text_plain"); 
	    mc.addMailcap("multipart/*;; x-java-content-handler=com.sun.mail.handlers.multipart_mixed"); 
	    mc.addMailcap("message/rfc822;; x-java-content-handler=com.sun.mail.handlers.message_rfc822"); 
	    CommandMap.setDefaultCommandMap(mc);
	}
	
	public EmailSender(String user, String password) {
		this();
		mUser = user;
		mPassword = password;
	}
	
	public boolean send() throws AddressException, MessagingException {
		Properties props = setProperties();
		
		if(!mUser.equals("") && !mPassword.equals("") && mTo.length > 0 && !mFrom.equals("") && !mSubject.equals("") && !mBody.equals("")) {
			Session session = Session.getInstance(props, this);
			
			MimeMessage msg = new MimeMessage(session);
			
			msg.setFrom(new InternetAddress(mFrom));
			
			InternetAddress[] addressTo = new InternetAddress[mTo.length];
			for (int i=0; i<mTo.length; i++)
				addressTo[i] = new InternetAddress(mTo[i]);
			
			msg.setRecipients(MimeMessage.RecipientType.TO, addressTo);
			msg.setSubject("Subject");
			msg.setSentDate(new Date());
			
			BodyPart messageBodyPart = new MimeBodyPart();
			messageBodyPart.setText("BODY");
			mMultipart.addBodyPart(messageBodyPart);
			
			msg.setContent(mMultipart);
			
			Transport.send(msg);
			return true;
		} else {
			return false;
		}
	}
	
	@Override
	public PasswordAuthentication getPasswordAuthentication() {
		return new PasswordAuthentication(mUser, mPassword);
	}

	private Properties setProperties() {
		Properties props = new Properties();
		
		props.put("mail.smtp.host", mHost);
		
		if (mDebuggable)
			props.put("mail.debug", "true");
		if (mAuth)
			props.put("mail.smtp.auth", "true");
		
		props.put("mail.smtp.port", mPort); 
		props.put("mail.smtp.socketFactory.port", mSPort); 
		props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory"); 
		props.put("mail.smtp.socketFactory.fallback", "false");
		
		return props;
	}

	public String getUser() {
		return mUser;
	}

	public void setUser(String user) {
		mUser = user;
	}

	public String getPassword() {
		return mPassword;
	}

	public void setPassword(String password) {
		mPassword = password;
	}

	public String[] getTo() {
		return mTo;
	}

	public void setTo(String[] to) {
		mTo = to;
	}

	public String getFrom() {
		return mFrom;
	}

	public void setFrom(String from) {
		mFrom = from;
	}

	public String getPort() {
		return mPort;
	}

	public void setPort(String port) {
		mPort = port;
	}

	public String getSPort() {
		return mSPort;
	}

	public void setSPort(String sPort) {
		mSPort = sPort;
	}

	public String getHost() {
		return mHost;
	}

	public void setHost(String host) {
		mHost = host;
	}

	public String getSubject() {
		return mSubject;
	}

	public void setSubject(String subject) {
		mSubject = subject;
	}

	public String getBody() {
		return mBody;
	}

	public void setBody(String body) {
		mBody = body;
	}

	public boolean isAuth() {
		return mAuth;
	}

	public void setAuth(boolean auth) {
		mAuth = auth;
	}

	public Multipart getMultipart() {
		return mMultipart;
	}

	public void setMultipart(Multipart multipart) {
		mMultipart = multipart;
	}
	
}
