package com.example.email;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.net.URL;
import java.util.Date;
import java.util.Iterator;
import java.util.Properties;
import java.util.Vector;
 











import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.Address;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.NoSuchProviderException;
import javax.mail.Part;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.event.ConnectionEvent;
import javax.mail.event.ConnectionListener;
import javax.mail.event.TransportEvent;
import javax.mail.event.TransportListener;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import org.apache.commons.exec.LogOutputStream;
import org.apache.log4j.Logger;

import com.example.database.DatabaseQuery;
import com.example.employee.Employee;
import com.example.energytrack2_0.Log4jContextListener;
import com.example.energytrack2_0.QuartzContextListener;
import com.example.programpreferences.ProgramPreferences;
import com.sun.mail.smtp.SMTPSendFailedException;
import com.vaadin.server.VaadinService;
import com.vaadin.ui.Notification;



public class EmailManagement{
	final static String emailAddress = ProgramPreferences.getEmailAddress(QuartzContextListener.context);
	final static Logger errorLogger = Logger.getLogger(EmailManagement.class);
	
	
	final static String username = ProgramPreferences.getEmailUser(QuartzContextListener.context);
	final static String password = ProgramPreferences.getEmailPass(QuartzContextListener.context);
	//final static String basepath = VaadinService.getCurrent()
            //.getBaseDirectory().getAbsolutePath()+"/WEB-INF/Resources/Templates/";
	final static String basepath = QuartzContextListener.context.getRealPath("/WEB-INF/Resources/Templates/");
	
	final static String wcgImg = "wcglogo.png";
public static Session getMailSession(){
	
	
	
	
	Properties props = new Properties();
	
	props.put("mail.smtp.auth","true");
	props.put("mail.smtp.ssl.trust","*");
	props.put("mail.smtp.starttls.enable","true");
	props.put("mail.smtp.host", ProgramPreferences.getHost(QuartzContextListener.context));
	props.put("mail.smtp.port", ProgramPreferences.getPort(QuartzContextListener.context));
	//props.put("mail.smtp.timeout",25000);
	//props.put("mail.smtp.connectiontimeout",25000);
	
	Session session = Session.getInstance(props,new javax.mail.Authenticator(){
		protected PasswordAuthentication getPasswordAuthentication(){
			return new PasswordAuthentication(username,password);}});
	
	LogOutputStream losStdOut = new LogOutputStream() {             
	    @Override
	    protected void processLine(String line, int level) {
	        errorLogger.debug(line);
	    }
	};
	
	session.setDebugOut(new PrintStream(losStdOut));
	session.setDebug(true);
	
	return session;
	}


public static void sendStageOneIntroEmail(){
	
	System.out.println("Sending project introduction emails (Stage 1)...");
	errorLogger.info("Sending project introduction emails (Stage 1)...");
	MailListener listener = null;
	Transport transport = null;
	Message message = null;
	int errorCount = 0;
	
	try {
		transport = getMailSession().getTransport("smtp");
		listener = new MailListener();
		transport.addTransportListener(listener);
		transport.connect(username,password);
		message = new MimeMessage(getMailSession());
		message.setFrom(new InternetAddress(emailAddress));
		Vector<Employee> groupEmployees = TemplateMarker.setStageOneIntroMessage();
		listener.totalEmails=groupEmployees.size();
		Iterator<Employee> employeeIterator = groupEmployees.iterator();
	while(employeeIterator.hasNext()){
			
			Employee employee = employeeIterator.next();
			if(isValidEmailAddress(employee.getEmail())){
				message.setRecipient(Message.RecipientType.CC,new InternetAddress(employee.getEmail()));
			}
			else{
				continue;
			};
			Multipart multipart = new MimeMultipart("related");
			BodyPart htmlPart = new MimeBodyPart();
			htmlPart.setContent(employee.getPersonalisedMessage(),"text/html; charset=UTF-8");
			multipart.addBodyPart(htmlPart);
			
			BodyPart wcgPart = new MimeBodyPart();
			wcgPart.setDataHandler(new DataHandler(new FileDataSource(basepath+wcgImg)));
			wcgPart.setHeader("Content-ID","<wcglogo>");
			multipart.addBodyPart(wcgPart);
			
			BodyPart bannerPart = new MimeBodyPart();
			bannerPart.setDataHandler(new DataHandler(new FileDataSource(basepath+"intro1_banner.png")));
			bannerPart.setHeader("Content-ID","<intro1_banner>");
			multipart.addBodyPart(bannerPart);
			
			
			message.setSubject("The 2Wise2Waste Electricity Savings Project Begins Next Week");
			message.setContent(multipart);
			message.saveChanges();
			
			try{
				if(transport.isConnected()==false){
					transport.connect(username,password);
				}
				transport.sendMessage(message,message.getAllRecipients());
			}
			catch(Exception e){
				if(errorCount<=20){
					errorLogger.error("There was a problem sending",e);
					try {
						transport.close();
						Thread.sleep(10000);
						
						transport.connect(username,password);
						transport.sendMessage(message,message.getAllRecipients());
					} catch (Exception e1) {
						e.printStackTrace();
						errorLogger.error("Error occured attempting to connect to SMTP server",e1);
					}
					errorCount++;
				}
				else{
					errorLogger.error("Sending emails has failed! Error count has reached threshold!");
					return;
				}
			}
		
		
		}
	transport.close();
	transport.removeTransportListener(listener);
	System.out.println("All introduction emails (stage 1) have been sent..");
	errorLogger.info("All introduction emails (stage 1) have been sent..");}
	
	catch(Exception e){
		System.out.println("ERROR (OPERATION NOT COMPLETE)");
		e.printStackTrace();
		errorLogger.error(e);	
	}
	
}

public static void sendStageTwoIntroEmailA(){
	System.out.println("Sending project introduction emails (Stage 2-Group A)...");
	errorLogger.info("Sending project introduction emails (Stage 2-Group A)...");
	int errorCount = 0;
	try {
		Transport transport = getMailSession().getTransport("smtp");
		MailListener listener = new MailListener();
		transport.addTransportListener(listener);
		transport.connect(username,password);
		Message message = new MimeMessage(getMailSession());
		message.setFrom(new InternetAddress(emailAddress));
		Vector<Employee> groupEmployees = TemplateMarker.setStageTwoIntroMessageA();
		Iterator<Employee> employeeIterator = groupEmployees.iterator();
		listener.totalEmails=groupEmployees.size();
	while(employeeIterator.hasNext()){
			
			Employee employee = employeeIterator.next();
			if(isValidEmailAddress(employee.getEmail())){
				message.setRecipient(Message.RecipientType.CC,new InternetAddress(employee.getEmail()));
			}
			else{
				continue;
			}
			Multipart multipart = new MimeMultipart("related");
			BodyPart htmlPart = new MimeBodyPart();
			htmlPart.setContent(employee.getPersonalisedMessage(),"text/html; charset=UTF-8");
			multipart.addBodyPart(htmlPart);
			BodyPart wcgPart = new MimeBodyPart();
			wcgPart.setDataHandler(new DataHandler(new FileDataSource(basepath+wcgImg)));
			wcgPart.setHeader("Content-ID","<wcglogo>");
			multipart.addBodyPart(wcgPart);
			
			BodyPart bannerPart = new MimeBodyPart();
			bannerPart.setDataHandler(new DataHandler(new FileDataSource(basepath+"intro2_banner.png")));
			bannerPart.setHeader("Content-ID","<intro2_banner>");
			multipart.addBodyPart(bannerPart);
			
			message.setSubject("The Second Phase of the 2Wise2Waste Electricity Savings Project Begins Next Week");
			message.setContent(multipart);
			message.saveChanges();
			try{
				if(transport.isConnected()==false){
					transport.connect(username,password);
				}
				transport.sendMessage(message,message.getAllRecipients());
			}
			catch(Exception e){
				if(errorCount<=20){
					errorLogger.error("There was a problem sending",e);
					try {
						transport.close();
						Thread.sleep(10000);
						
						transport.connect(username,password);
						transport.sendMessage(message,message.getAllRecipients());
					} catch (Exception e1) {
						e.printStackTrace();
						errorLogger.error("Error occured attempting to connect to SMTP server",e1);
					}
					errorCount++;
				}
				else{
					errorLogger.error("Sending emails has failed! Error count has reached threshold!");
					return;
				}
			}
		
		
		}
	transport.close();
	transport.removeTransportListener(listener);
	System.out.println("All introduction emails (stage 2- Group A) have been sent..");
	errorLogger.info("All introduction emails (stage 2- Group A) have been sent..");
		
		
	}catch(Exception e){
		System.out.println("ERROR (OPERATION NOT COMPLETE)");
		errorLogger.error(e);	
	}
	
}

public static void sendStageTwoIntroEmailB(){
	int errorCount = 0;
	System.out.println("Sending project introduction emails (Stage 2-Group B)...");
	errorLogger.info("Sending project introduction emails (Stage 2-Group B)...");
	try {
		Transport transport = getMailSession().getTransport("smtp");
		MailListener listener = new MailListener();
		transport.addTransportListener(listener);
		transport.connect(username,password);
		Message message = new MimeMessage(getMailSession());
		message.setFrom(new InternetAddress(emailAddress));
		Vector<Employee> groupEmployees = TemplateMarker.setStageTwoIntroMessageB();
		listener.totalEmails=groupEmployees.size();
		Iterator<Employee> employeeIterator = groupEmployees.iterator();
	while(employeeIterator.hasNext()){
			
			Employee employee = employeeIterator.next();
			if(isValidEmailAddress(employee.getEmail())){
				message.setRecipient(Message.RecipientType.CC,new InternetAddress(employee.getEmail()));
			}
			else{
				continue;
			}
			Multipart multipart = new MimeMultipart("related");
			BodyPart htmlPart = new MimeBodyPart();
			htmlPart.setContent(employee.getPersonalisedMessage(),"text/html; charset=UTF-8");
			multipart.addBodyPart(htmlPart);
			BodyPart wcgPart = new MimeBodyPart();
			wcgPart.setDataHandler(new DataHandler(new FileDataSource(basepath+wcgImg)));
			wcgPart.setHeader("Content-ID","<wcglogo>");
			multipart.addBodyPart(wcgPart);
			
			BodyPart bannerPart = new MimeBodyPart();
			bannerPart.setDataHandler(new DataHandler(new FileDataSource(basepath+"intro2_banner.png")));
			bannerPart.setHeader("Content-ID","<intro2_banner>");
			multipart.addBodyPart(bannerPart);
			
			message.setSubject("The Second Phase of the 2Wise2Waste Electricity Savings Project Begins Next Week");
			message.setContent(multipart);
			message.saveChanges();
			try{
				if(transport.isConnected()==false){
					transport.connect(username,password);
				}
				transport.sendMessage(message,message.getAllRecipients());
			}
			catch(Exception e){
				if(errorCount<=20){
					errorLogger.error("There was a problem sending",e);
					try {
						transport.close();
						Thread.sleep(10000);
						
						transport.connect(username,password);
						transport.sendMessage(message,message.getAllRecipients());
					} catch (Exception e1) {
						e.printStackTrace();
						errorLogger.error("Error occured attempting to connect to SMTP server",e1);
					}
					errorCount++;
				}
				else{
					errorLogger.error("Sending emails has failed! Error count has reached threshold!");
					return;
				}
			}
		
		
		}
	transport.close();
	transport.removeTransportListener(listener);
	System.out.println("All introduction emails (stage 2- Group B) have been sent..");
	errorLogger.info("All introduction emails (stage 2- Group B) have been sent..");
		
		
	} catch(Exception e){
		System.out.println("ERROR (OPERATION NOT COMPLETE)");
		e.printStackTrace();
		errorLogger.error(e);	
	}
	
}

public static void sendAfternoonReminder(){
	System.out.println("Sending Afternoon Reminders...");
	int errorCount = 0;
	try {
		Transport transport = getMailSession().getTransport("smtp");
		MailListener listener = new MailListener();
		transport.addTransportListener(listener);
		transport.connect(username,password);
		Message message = new MimeMessage(getMailSession());
		message.setFrom(new InternetAddress(emailAddress));
		Vector<Employee> groupEmployees = TemplateMarker.setAfternoonReminder();
		listener.totalEmails=groupEmployees.size();
		Iterator<Employee> employeeIterator = groupEmployees.iterator();
	while(employeeIterator.hasNext()){
			
			Employee employee = employeeIterator.next();
			if(isValidEmailAddress(employee.getEmail())){
				message.setRecipient(Message.RecipientType.CC,new InternetAddress(employee.getEmail()));
			}
			else{
				continue;
			}
			Multipart multipart = new MimeMultipart("related");
			BodyPart htmlPart = new MimeBodyPart();
			htmlPart.setContent(employee.getPersonalisedMessage(),"text/html; charset=UTF-8");
			multipart.addBodyPart(htmlPart);
			BodyPart wcgPart = new MimeBodyPart();
			wcgPart.setDataHandler(new DataHandler(new FileDataSource(basepath+wcgImg)));
			wcgPart.setHeader("Content-ID","<wcglogo>");
			multipart.addBodyPart(wcgPart);
			
			BodyPart bannerPart = new MimeBodyPart();
			bannerPart.setDataHandler(new DataHandler(new FileDataSource(basepath+"afternoon_banner.png")));
			bannerPart.setHeader("Content-ID","<afternoon_banner>");
			multipart.addBodyPart(bannerPart);
			
			message.setSubject("Remember to switch off all office equipment and appliances");
			message.setContent(multipart);
			message.saveChanges();
			try{
				if(transport.isConnected()==false){
					transport.connect(username,password);
				}
				transport.sendMessage(message,message.getAllRecipients());
			}
			catch(Exception e){
				if(errorCount<=20){
					errorLogger.error("There was a problem sending",e);
					try {
						transport.close();
						Thread.sleep(10000);
						
						transport.connect(username,password);
						transport.sendMessage(message,message.getAllRecipients());
					} catch (Exception e1) {
						e.printStackTrace();
						errorLogger.error("Error occured attempting to connect to SMTP server",e1);
					}
					errorCount++;
				}
				else{
					errorLogger.error("Sending emails has failed! Error count has reached threshold!");
					return;
				}
			}
		
		
		}
	transport.close();
	transport.removeTransportListener(listener);
	System.out.println("\nAll afternoon reminders have been sent..");
	errorLogger.info("\nAll afternoon reminders have been sent..");
		
		
	}catch(Exception e){
		e.printStackTrace();
		System.out.println("ERROR (OPERATION NOT COMPLETE)");
		errorLogger.error(e);	
	}
	
}

public static void sendEmployeeTipEmailGroupA(){
	System.out.println("Sending energy tip emails to Group A...");
	errorLogger.info("Sending energy tip emails to Group A...");
	int errorCount=0;
	try {
		Transport transport = getMailSession().getTransport("smtp");
		MailListener listener = new MailListener();
		transport.addTransportListener(listener);
		transport.connect(username,password);
		Message message = new MimeMessage(getMailSession());
		message.setFrom(new InternetAddress(emailAddress));
		Vector<Employee> groupEmployees = TemplateMarker.setEmployeeTipEmailGroupA();
		listener.totalEmails=groupEmployees.size();
		Iterator<Employee> employeeIterator = groupEmployees.iterator();
	while(employeeIterator.hasNext()){
			
			Employee employee = employeeIterator.next();
			if(isValidEmailAddress(employee.getEmail())){
				message.setRecipient(Message.RecipientType.CC,new InternetAddress(employee.getEmail()));
			}
			else{
				continue;
			}
			Multipart multipart = new MimeMultipart("related");
			BodyPart htmlPart = new MimeBodyPart();
			htmlPart.setContent(employee.getPersonalisedMessage(),"text/html; charset=UTF-8");
			multipart.addBodyPart(htmlPart);
			BodyPart wcgPart = new MimeBodyPart();
			wcgPart.setDataHandler(new DataHandler(new FileDataSource(basepath+wcgImg)));
			wcgPart.setHeader("Content-ID","<wcglogo>");
			multipart.addBodyPart(wcgPart);
			
			BodyPart bannerPart = new MimeBodyPart();
			bannerPart.setDataHandler(new DataHandler(new FileDataSource(basepath+"tips_banner.png")));
			bannerPart.setHeader("Content-ID","<tips_banner>");
			multipart.addBodyPart(bannerPart);
			
			message.setSubject("How to save electricity on your floor");
			message.setContent(multipart);
			message.saveChanges();
			try{
				if(transport.isConnected()==false){
					transport.connect(username,password);
				}
				transport.sendMessage(message,message.getAllRecipients());
			}
			catch(Exception e){
				if(errorCount<=20){
					errorLogger.error("There was a problem sending",e);
					try {
						transport.close();
						Thread.sleep(100000);
						
						transport.connect(username,password);
						transport.sendMessage(message,message.getAllRecipients());
					} catch (Exception e1) {
						e.printStackTrace();
						errorLogger.error("Error occured attempting to connect to SMTP server",e1);
					}
					errorCount++;
				}
				else{
					errorLogger.error("Sending emails has failed! Error count has reached threshold!");
					return;
				}
			}
		
		
		}
	transport.close();
	transport.removeTransportListener(listener);
	System.out.println("All energy tip emails have been sent to Group A..");
	errorLogger.info("All energy tip emails have been sent to Group A..");
		
		
	}catch(Exception e){
		e.printStackTrace();
		System.out.println("ERROR (OPERATION NOT COMPLETE)");
		errorLogger.error(e);	
	}
	
	
}

public static void sendEmployeeTipEmailGroupB(){
	System.out.println("Sending energy tip emails to Group B...");
	errorLogger.info("Sending energy tip emails to Group B...");
	int errorCount=0;
	try {
		Transport transport = getMailSession().getTransport("smtp");
		MailListener listener = new MailListener();
		transport.addTransportListener(listener);
		transport.connect(username,password);
		Message message = new MimeMessage(getMailSession());
		message.setFrom(new InternetAddress(emailAddress));
		Vector<Employee> groupEmployees = TemplateMarker.setEmployeeTipEmailGroupB();
		listener.totalEmails=groupEmployees.size();
		Iterator<Employee> employeeIterator = groupEmployees.iterator();
	while(employeeIterator.hasNext()){
			
			Employee employee = employeeIterator.next();
			if(isValidEmailAddress(employee.getEmail())){
				message.setRecipient(Message.RecipientType.CC,new InternetAddress(employee.getEmail()));
			}
			else{
				continue;
			}
			Multipart multipart = new MimeMultipart("related");
			BodyPart htmlPart = new MimeBodyPart();
			htmlPart.setContent(employee.getPersonalisedMessage(),"text/html; charset=UTF-8");
			multipart.addBodyPart(htmlPart);
			BodyPart wcgPart = new MimeBodyPart();
			wcgPart.setDataHandler(new DataHandler(new FileDataSource(basepath+wcgImg)));
			wcgPart.setHeader("Content-ID","<wcglogo>");
			multipart.addBodyPart(wcgPart);
			
			BodyPart bannerPart = new MimeBodyPart();
			bannerPart.setDataHandler(new DataHandler(new FileDataSource(basepath+"tips_banner.png")));
			bannerPart.setHeader("Content-ID","<tips_banner>");
			multipart.addBodyPart(bannerPart);
			
			message.setSubject("How to save electricity on your floor");
			message.setContent(multipart);
			message.saveChanges();
			try{
				if(transport.isConnected()==false){
					transport.connect(username,password);
				}
				transport.sendMessage(message,message.getAllRecipients());
			}
			catch(Exception e){
				if(errorCount<=20){
					errorLogger.error("There was a problem sending",e);
					try {
						transport.close();
						Thread.sleep(100000);
						
						transport.connect(username,password);
						transport.sendMessage(message,message.getAllRecipients());
					} catch (Exception e1) {
						e.printStackTrace();
						errorLogger.error("Error occured attempting to connect to SMTP server",e1);
					}
					errorCount++;
				}
				else{
					errorLogger.error("Sending emails has failed! Error count has reached threshold!");
					return;
				}
			}
		
		
		}
	transport.close();
	transport.removeTransportListener(listener);
	System.out.println("All energy tip emails have been sent to Group B..");
	errorLogger.info("All energy tip emails have been sent to Group B..");
		
		
	}catch(Exception e){
		e.printStackTrace();
		System.out.println("ERROR (OPERATION NOT COMPLETE)");
		errorLogger.error(e);	
	}
	
	
}



public static void energyAdvocateEmail(){
	int errorCount = 0;
	try{
		System.out.println("Sending emails notifying energy advocates of their role..");
		errorLogger.info("Sending emails notifying energy advocates of their role..");
		Message message = new MimeMessage(getMailSession());
		message.setFrom(new InternetAddress(emailAddress));
		Vector<Employee> groupEmployees = TemplateMarker.setEnergyAdvocateEmail();
		Iterator<Employee> employeeIterator = groupEmployees.iterator();
		
		Transport transport = getMailSession().getTransport("smtp");
		MailListener listener = new MailListener();
		listener.totalEmails=groupEmployees.size();
		transport.addTransportListener(listener);
		transport.connect(username,password);
		
		while(employeeIterator.hasNext()){
			Employee employee = employeeIterator.next();
			if(isValidEmailAddress(employee.getEmail())){
				message.setRecipient(Message.RecipientType.CC,new InternetAddress(employee.getEmail()));
			}
			else{
				continue;
			}
			Multipart multipart = new MimeMultipart("related");
			BodyPart htmlPart = new MimeBodyPart();
			htmlPart.setContent(employee.getPersonalisedMessage(),"text/html; charset=UTF-8");
			multipart.addBodyPart(htmlPart);
			
			BodyPart wcgPart = new MimeBodyPart();
			wcgPart.setDataHandler(new DataHandler(new FileDataSource(basepath+wcgImg)));
			wcgPart.setHeader("Content-ID","<wcglogo>");
			multipart.addBodyPart(wcgPart);
			
			BodyPart bannerPart = new MimeBodyPart();
			bannerPart.setDataHandler(new DataHandler(new FileDataSource(basepath+"advocateA_banner.png")));
			bannerPart.setHeader("Content-ID","<intro1_banner>");
			multipart.addBodyPart(bannerPart);
			
			message.setSubject("You are this weeks Energy Savings Advocate");
			message.setContent(multipart);
			message.saveChanges();
			try{
				if(transport.isConnected()==false){
					transport.connect(username,password);
				}
				transport.sendMessage(message,message.getAllRecipients());
			}
			catch(Exception e){
				if(errorCount<=20){
					errorLogger.error("There was a problem sending",e);
					try {
						transport.close();
						Thread.sleep(10000);
						
						transport.connect(username,password);
						transport.sendMessage(message,message.getAllRecipients());
					} catch (Exception e1) {
						e.printStackTrace();
						errorLogger.error("Error occured attempting to connect to SMTP server",e1);
					}
					errorCount++;
				}
				else{
					errorLogger.error("Sending emails has failed! Error count has reached threshold!");
					return;
				}
			}
		
		}
		System.out.println("Emails notifying energy advocates of their role have been sent.");
		errorLogger.info("Emails notifying energy advocates of their role have been sent.");
		transport.close();
		transport.removeTransportListener(listener);
		
	}
	 catch (Exception e) {
		 	System.out.println("ERROR (OPERATION NOT COMPLETE)");
		 	e.printStackTrace();
		 	errorLogger.error(e);
		 	
		}
	
		
	
	
}

public static void sendAdvocateNotiEmail(){
	int errorCount = 0;
	try{
		System.out.println("Sending emails notifying employees of new advocates...");
		errorLogger.info("Sending emails notifying employees of new advocates...");
		Message message = new MimeMessage(getMailSession());
		message.setFrom(new InternetAddress(emailAddress));
		
		Transport transport = getMailSession().getTransport("smtp");
		MailListener listener = new MailListener();
		transport.addTransportListener(listener);
		
		transport.connect(username,password);
		
		Vector<Employee> groupEmployees = TemplateMarker.setAdvocateNotiEmail();
		listener.totalEmails=groupEmployees.size();
		Iterator<Employee> employeeIterator = groupEmployees.iterator();
		while(employeeIterator.hasNext()){
			Employee employee = employeeIterator.next();
			if(isValidEmailAddress(employee.getEmail())){
				message.setRecipient(Message.RecipientType.CC,new InternetAddress(employee.getEmail()));
			}
			else{
				continue;
			}
			Multipart multipart = new MimeMultipart("related");
			BodyPart htmlPart = new MimeBodyPart();
			htmlPart.setContent(employee.getPersonalisedMessage(),"text/html; charset=UTF-8");
			multipart.addBodyPart(htmlPart);
			
			BodyPart wcgPart = new MimeBodyPart();
			wcgPart.setDataHandler(new DataHandler(new FileDataSource(basepath+wcgImg)));
			wcgPart.setHeader("Content-ID","<wcglogo>");
			multipart.addBodyPart(wcgPart);
			
			BodyPart bannerPart = new MimeBodyPart();
			bannerPart.setDataHandler(new DataHandler(new FileDataSource(basepath+"advocateB_banner.png")));
			bannerPart.setHeader("Content-ID","<intro1_banner>");
			multipart.addBodyPart(bannerPart);;
			
			message.setSubject("Your Energy Savings Advocate this week is");
			message.setContent(multipart);
			message.saveChanges();
			try{
				if(transport.isConnected()==false){
					transport.connect(username,password);
				}
				transport.sendMessage(message,message.getAllRecipients());
			}
			catch(Exception e){
				if(errorCount<=20){
					errorLogger.error("There was a problem sending",e);
					try {
						transport.close();
						Thread.sleep(10000);
						
						transport.connect(username,password);
						transport.sendMessage(message,message.getAllRecipients());
					} catch (Exception e1) {
						e.printStackTrace();
						errorLogger.error("Error occured attempting to connect to SMTP server",e1);
					}
					errorCount++;
				}
				else{
					errorLogger.error("Sending emails has failed! Error count has reached threshold!");
					return;
				}
			}
		}
		transport.close();
		transport.removeTransportListener(listener);
		
	}
	 catch (Exception e) {
		 	System.out.println("ERROR (OPERATION NOT COMPLETE)");
		 	e.printStackTrace();
		 	errorLogger.error(e);
		}
	
	
	
	
System.out.println("Emails notifying employees of new advocates have been sent");
errorLogger.info("Emails notifying employees of new advocates have been sent");
}

public static void sendCompetitionNoAdvocateEmail(){
	int errorCount = 0;
	try{
		System.out.println("Sending interfloor competition emails to group A...");
		errorLogger.info("Sending interfloor competition emails to group A...");
		Message message = new MimeMessage(getMailSession());
		message.setFrom(new InternetAddress(emailAddress));
		
		Transport transport = getMailSession().getTransport("smtp");
		MailListener listener = new MailListener();
		transport.addTransportListener(listener);
		transport.connect(username,password);
		
		Vector<Employee> groupEmployees = TemplateMarker.setCompetitionNoAdvocateEmail();
		listener.totalEmails=groupEmployees.size();
		Iterator<Employee> employeeIterator = groupEmployees.iterator();
		while(employeeIterator.hasNext()){
			Employee employee = employeeIterator.next();
			if(isValidEmailAddress(employee.getEmail())){
				message.setRecipient(Message.RecipientType.CC,new InternetAddress(employee.getEmail()));
			}
			else{
				continue;
			}
			Multipart multipart = new MimeMultipart("related");
			BodyPart htmlPart = new MimeBodyPart();
			htmlPart.setContent(employee.getPersonalisedMessage(),"text/html; charset=UTF-8");
			multipart.addBodyPart(htmlPart);
			
			BodyPart wcgPart = new MimeBodyPart();
			wcgPart.setDataHandler(new DataHandler(new FileDataSource(basepath+wcgImg)));
			wcgPart.setHeader("Content-ID","<wcglogo>");
			multipart.addBodyPart(wcgPart);
			
			BodyPart bannerPart = new MimeBodyPart();
			bannerPart.setDataHandler(new DataHandler(new FileDataSource(basepath+"winner_banner.png")));
			bannerPart.setHeader("Content-ID","<intro1_banner>");
			multipart.addBodyPart(bannerPart);
			
			message.setSubject("See who won last weeks Electricity Savings Competition! How did your floor do?");
			message.setContent(multipart);
			message.saveChanges();
			try{
				if(transport.isConnected()==false){
					transport.connect(username,password);
				}
				transport.sendMessage(message,message.getAllRecipients());
			}
			catch(Exception e){
				if(errorCount<=20){
					errorLogger.error("There was a problem sending",e);
					try {
						transport.close();
						Thread.sleep(10000);
						
						transport.connect(username,password);
						transport.sendMessage(message,message.getAllRecipients());
					} catch (Exception e1) {
						e.printStackTrace();
						errorLogger.error("Error occured attempting to connect to SMTP server",e1);
					}
					errorCount++;
				}
				else{
					errorLogger.error("Sending emails has failed! Error count has reached threshold!");
					return;
				}
			}
		}
		transport.close();
		transport.removeTransportListener(listener);
		
	}
	 catch (Exception e) {
		 	System.out.println("ERROR (OPERATION NOT COMPLETE)");
		 	errorLogger.error(e);
			e.printStackTrace();
		}
	
	
	
	
System.out.println("Interfloor competition emails to group A have been sent.");
errorLogger.info("Interfloor competition emails to group A have been sent.");
}

public static void sendCompetitionAdvocateEmail(){
	int errorCount = 0;
	try{
		System.out.println("Sending interfloor competition emails to group B...");
		errorLogger.info("Sending interfloor competition emails to group B...");
		Message message = new MimeMessage(getMailSession());
		message.setFrom(new InternetAddress(emailAddress));
		
		Transport transport = getMailSession().getTransport("smtp");
		MailListener listener = new MailListener();
		transport.addTransportListener(listener);
		transport.connect(username,password);
		
		Vector<Employee> groupEmployees = TemplateMarker.setCompetitionAdvocateEmail();
		listener.totalEmails=groupEmployees.size();
		Iterator<Employee> employeeIterator = groupEmployees.iterator();
		while(employeeIterator.hasNext()){
			Employee employee = employeeIterator.next();
			if(isValidEmailAddress(employee.getEmail())){
				message.setRecipient(Message.RecipientType.CC,new InternetAddress(employee.getEmail()));
			}
			else{
				continue;
			}
			Multipart multipart = new MimeMultipart("related");
			BodyPart htmlPart = new MimeBodyPart();
			htmlPart.setContent(employee.getPersonalisedMessage(),"text/html; charset=UTF-8");
			multipart.addBodyPart(htmlPart);
			
			BodyPart wcgPart = new MimeBodyPart();
			wcgPart.setDataHandler(new DataHandler(new FileDataSource(basepath+wcgImg)));
			wcgPart.setHeader("Content-ID","<wcglogo>");
			multipart.addBodyPart(wcgPart);
			
			BodyPart bannerPart = new MimeBodyPart();
			bannerPart.setDataHandler(new DataHandler(new FileDataSource(basepath+"winner_banner.png")));
			bannerPart.setHeader("Content-ID","<intro1_banner>");
			multipart.addBodyPart(bannerPart);
			
			message.setSubject("See who won last weeks Electricity Savings Competition! How did your floor do?");
			message.setContent(multipart);
			message.saveChanges();
			try{
				if(transport.isConnected()==false){
					transport.connect(username,password);
				}
				transport.sendMessage(message,message.getAllRecipients());
			}
			catch(Exception e){
				if(errorCount<=20){
					errorLogger.error("There was a problem sending",e);
					try {
						transport.close();
						Thread.sleep(10000);
						
						transport.connect(username,password);
						transport.sendMessage(message,message.getAllRecipients());
					} catch (Exception e1) {
						e.printStackTrace();
						errorLogger.error("Error occured attempting to connect to SMTP server",e1);
					}
					errorCount++;
				}
				else{
					errorLogger.error("Sending emails has failed! Error count has reached threshold!");
					return;
				}
			}
		}
		transport.close();
		transport.removeTransportListener(listener);
		
	}
	 catch (Exception e) {
		 	System.out.println("ERROR (OPERATION NOT COMPLETE)");
		 	errorLogger.error(e);
			e.printStackTrace();
		}
	
	
	
	
System.out.println("Interfloor competition emails to group B have been sent.");
errorLogger.info("Interfloor competition emails to group B have been sent.");
}

public static void sendKitchenTipEmailGroupA(){
	int errorCount=0;
	try{
		System.out.println("Sending kitchen tip emails to Group A...");
		errorLogger.info("Sending kitchen tip emails to Group A...");
		Message message = new MimeMessage(getMailSession());
		message.setFrom(new InternetAddress(emailAddress));
		
		Transport transport = getMailSession().getTransport("smtp");
		MailListener listener = new MailListener();
		transport.addTransportListener(listener);
		transport.connect(username,password);
		
		Vector<Employee> groupEmployees = TemplateMarker.setKitchenTipEmailGroupA();
		listener.totalEmails=groupEmployees.size();
		Iterator<Employee> employeeIterator = groupEmployees.iterator();
		while(employeeIterator.hasNext()){
			Employee employee = employeeIterator.next();
			if(isValidEmailAddress(employee.getEmail())){
				message.setRecipient(Message.RecipientType.CC,new InternetAddress(employee.getEmail()));
			}
			else{
				continue;
			}
			
			Multipart multipart = new MimeMultipart("related");
			BodyPart htmlPart = new MimeBodyPart();
			htmlPart.setContent(employee.getPersonalisedMessage(),"text/html; charset=UTF-8");
			multipart.addBodyPart(htmlPart);
			
			BodyPart wcgPart = new MimeBodyPart();
			wcgPart.setDataHandler(new DataHandler(new FileDataSource(basepath+wcgImg)));
			wcgPart.setHeader("Content-ID","<wcglogo>");
			multipart.addBodyPart(wcgPart);
			
			BodyPart bannerPart = new MimeBodyPart();
			bannerPart.setDataHandler(new DataHandler(new FileDataSource(basepath+"kitchen_banner.png")));
			bannerPart.setHeader("Content-ID","<kitchen_banner>");
			multipart.addBodyPart(bannerPart);
			
			message.setSubject("Reduce electricity use in the kitchen" );
			message.setContent(multipart);
			message.saveChanges();
			try{
				if(transport.isConnected()==false){
					transport.connect(username,password);
				}
				transport.sendMessage(message,message.getAllRecipients());
			}
			catch(Exception e){
				if(errorCount<=20){
					errorLogger.error("There was a problem sending",e);
					try {
						transport.close();
						Thread.sleep(10000);
						
						transport.connect(username,password);
						transport.sendMessage(message,message.getAllRecipients());
					} catch (Exception e1) {
						e.printStackTrace();
						errorLogger.error("Error occured attempting to connect to SMTP server",e1);
					}
					errorCount++;
				}
				else{
					errorLogger.error("Sending emails has failed! Error count has reached threshold!");
					return;
				}
			}
		transport.close();
		transport.removeTransportListener(listener);
		}}
	 catch (Exception e) {
		 	System.out.println("ERROR (OPERATION NOT COMPLETE)");
		 	errorLogger.error(e);
		 	e.printStackTrace();
		}
	

System.out.println("Kitchen tip emails have been sent to Group A.");
errorLogger.info("Kitchen tip emails have been sent to Group A.");
}

public static void sendKitchenTipEmailGroupB(){
	int errorCount=0;
	try{
		System.out.println("Sending kitchen tip emails to Group B...");
		errorLogger.info("Sending kitchen tip emails to Group B...");
		Message message = new MimeMessage(getMailSession());
		message.setFrom(new InternetAddress(emailAddress));
		
		Transport transport = getMailSession().getTransport("smtp");
		MailListener listener = new MailListener();
		transport.addTransportListener(listener);
		transport.connect(username,password);
		
		Vector<Employee> groupEmployees = TemplateMarker.setKitchenTipEmailGroupB();
		listener.totalEmails=groupEmployees.size();
		Iterator<Employee> employeeIterator = groupEmployees.iterator();
		while(employeeIterator.hasNext()){
			Employee employee = employeeIterator.next();
			if(isValidEmailAddress(employee.getEmail())){
				message.setRecipient(Message.RecipientType.CC,new InternetAddress(employee.getEmail()));
			}
			else{
				continue;
			}
			
			Multipart multipart = new MimeMultipart("related");
			BodyPart htmlPart = new MimeBodyPart();
			htmlPart.setContent(employee.getPersonalisedMessage(),"text/html; charset=UTF-8");
			multipart.addBodyPart(htmlPart);
			
			BodyPart wcgPart = new MimeBodyPart();
			wcgPart.setDataHandler(new DataHandler(new FileDataSource(basepath+wcgImg)));
			wcgPart.setHeader("Content-ID","<wcglogo>");
			multipart.addBodyPart(wcgPart);
			
			BodyPart bannerPart = new MimeBodyPart();
			bannerPart.setDataHandler(new DataHandler(new FileDataSource(basepath+"kitchen_banner.png")));
			bannerPart.setHeader("Content-ID","<kitchen_banner>");
			multipart.addBodyPart(bannerPart);
			
			message.setSubject("Reduce electricity use in the kitchen" );
			message.setContent(multipart);
			message.saveChanges();
			try{
				if(transport.isConnected()==false){
					transport.connect(username,password);
				}
				transport.sendMessage(message,message.getAllRecipients());
			}
			catch(Exception e){
				if(errorCount<=20){
					errorLogger.error("There was a problem sending",e);
					try {
						transport.close();
						Thread.sleep(10000);
						
						transport.connect(username,password);
						transport.sendMessage(message,message.getAllRecipients());
					} catch (Exception e1) {
						e.printStackTrace();
						errorLogger.error("Error occured attempting to connect to SMTP server",e1);
					}
					errorCount++;
				}
				else{
					errorLogger.error("Sending emails has failed! Error count has reached threshold!");
					return;
				}
			}
		transport.close();
		transport.removeTransportListener(listener);
		}}
	 catch (Exception e) {
		 	System.out.println("ERROR (OPERATION NOT COMPLETE)");
		 	errorLogger.error(e);
		 	e.printStackTrace();
		}
	

System.out.println("Kitchen tip emails have been sent to Group B.");
errorLogger.info("Kitchen tip emails have been sent to Group B.");
}


public static boolean isValidEmailAddress(String email) {
    if(email != null){
    	boolean result = true;
    	try {
    		InternetAddress emailAddr = new InternetAddress(email);
    		emailAddr.validate();
    	} catch (AddressException ex) {
    		errorLogger.info("Email address not valid!: "+email);
    		result = false;
    	}
    	return result;
    }
	else{
		return false;
}}

    }

