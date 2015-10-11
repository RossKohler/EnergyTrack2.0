package com.example.email;
import javax.mail.Address;
import javax.mail.event.ConnectionEvent;
import javax.mail.event.ConnectionListener;
import javax.mail.event.TransportEvent;
import javax.mail.event.TransportListener;
import javax.mail.internet.InternetAddress;

import org.apache.log4j.Logger;

import com.example.energytrack2_0.Log4jContextListener;
import com.vaadin.ui.Notification;

public class MailListener implements ConnectionListener,TransportListener {
	
	final static Logger infoLogger = Logger.getLogger(MailListener.class);
	@Override
	public void messageDelivered(TransportEvent e) {
	        if (e != null) {
	            Address[] a = e.getValidSentAddresses();
	        if (a != null && a.length > 0) {
	            for (int i = 0; i < a.length; i++) {
	            	infoLogger.info("Email to "+((InternetAddress) a[i]).getAddress()+" delivered!");
	                //System.out.println("Email to "+((InternetAddress) a[i]).getAddress()+" delivered!");
	            }
	        }
	        }
		
	}

	@Override
	public void messageNotDelivered(TransportEvent e) {
		   if (e != null) {
	            Address[] a = e.getValidUnsentAddresses();
	        if (a != null && a.length > 0) {
	            for (int i = 0; i < a.length; i++) {
	            	infoLogger.info("Email to "+((InternetAddress) a[i]).getAddress()+" not delivered!");
	                //System.out.println("Email to "+((InternetAddress) a[i]).getAddress()+" not delivered!");
	            }
	        }
	        }
		
	}

	@Override
	public void messagePartiallyDelivered(TransportEvent arg0) {
		// TODO Auto-generated method stub
	}

	@Override
	public void closed(ConnectionEvent arg0) {
		// TODO Auto-generated method stub
	}

	@Override
	public void disconnected(ConnectionEvent arg0) {
		// TODO Auto-generated method stub
	}

	@Override
	public void opened(ConnectionEvent arg0) {
		
		// TODO Auto-generated method stub
		
	}

	
}
