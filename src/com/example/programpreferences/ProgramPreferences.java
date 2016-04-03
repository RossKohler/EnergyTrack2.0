package com.example.programpreferences;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import javax.servlet.ServletContext;

import org.apache.log4j.Logger;

import com.example.energytrack2_0.Log4jContextListener;
import com.example.energytrack2_0.QuartzContextListener;
import com.vaadin.server.VaadinService;



public class ProgramPreferences{
	
	final static Logger errorLogger = Logger.getLogger(Log4jContextListener.class);
	private static String url = "/WEB-INF/Resources/config.properties";
	
	public static Properties openProperties(){
		Properties prop = new Properties();
		try {
			
			String basepath = VaadinService.getCurrent()
	                  .getBaseDirectory().getAbsolutePath();
			FileInputStream input = new FileInputStream(basepath+url);
			prop.load(input);
			input.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			errorLogger.error("Error in ProgramPreferences",e);
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			errorLogger.error("Error in ProgramPreferences",e);
			e.printStackTrace();
		}
		
		catch(Exception e){
			errorLogger.error("Error in ProgramPreferences",e);
			e.printStackTrace();
			
		}
		return prop;
	
		}
	
	public static Properties openProperties(ServletContext context){
		Properties prop = new Properties();
		try{
		FileInputStream input = new FileInputStream(context.getRealPath(url));
		prop.load(input);
		input.close();
	} catch (FileNotFoundException e) {
		// TODO Auto-generated catch block
		errorLogger.error("Error in ProgramPreferences",e);
		e.printStackTrace();
	} catch (IOException e) {
		// TODO Auto-generated catch block
		errorLogger.error("Error in ProgramPreferences",e);
		e.printStackTrace();
	}
	
	catch(Exception e){
		errorLogger.error("Error in ProgramPreferences",e);
		e.printStackTrace();
		
	}
		return prop;
	}

	
	
	public static String getDBUser(){
		Properties prop = openProperties();
		String dbUser = "";
		if(prop!= null){
			dbUser=prop.getProperty("dbUser");
		
		}
		return dbUser;
		}
	
	
	public static String getDBURL(){
		Properties prop = openProperties();
		String dbURL = "";
		if(prop!= null){
			dbURL=prop.getProperty("dbURL");
		}
		return dbURL;
		}
	
	public static String getDBPass(){
		Properties prop = openProperties();
		String dbPass = "";
		if(prop!= null){
			dbPass=prop.getProperty("dbPass");
		}
		return dbPass;
		}
	
	public static String getEmailAddress(){
		Properties prop = openProperties();
		String emailAddress = "";
		if(prop!= null){
			emailAddress=prop.getProperty("emailAddress");
		}
		return emailAddress;
		}
	
	public static String getEmailAddress(ServletContext context){
		Properties prop = openProperties(context);
		String emailAddress = null;
		if(prop!= null){
			emailAddress=prop.getProperty("emailAddress");
		}
		return emailAddress;
		}
	
	public static String getEmailUser(){
		Properties prop = openProperties();
		String emailUser = "";
		if(prop!= null){
			emailUser=prop.getProperty("emailUser");
		
		}
		return emailUser;
		}
	
	public static String getEmailUser(ServletContext context){
		Properties prop = openProperties(context);
		String emailUser = "";
		if(prop!= null){
			emailUser=prop.getProperty("emailUser");
		}
		return emailUser;
		}
	
	public static String getEmailPass(){
		Properties prop = openProperties();;
		String emailPass = "";
		if(prop!= null){
			emailPass=prop.getProperty("emailPass");
		
		}
		return emailPass;
		}
	
	public static String getEmailPass(ServletContext context){
		Properties prop = openProperties(context);
		String emailPass = "";
		if(prop!= null){
			emailPass=prop.getProperty("emailPass");
		
		}
		return emailPass;
		}
	
	
	public static String getHost(){
		Properties prop = openProperties();
		String hostname = "";
		if(prop!= null){
			hostname=prop.getProperty("emailHost");
		
		}
		return hostname;
		}
	
	public static String getHost(ServletContext context){
		Properties prop = openProperties(context);
		String hostname = "";
		if(prop!= null){
			hostname=prop.getProperty("emailHost");
		}
		return hostname;
		}
	
	public static String getPort(){
		Properties prop = openProperties();
		String port = "";
		if(prop!= null){
			port =prop.getProperty("emailPort");
		
		}
		return port;
		}
	
	public static String getPort(ServletContext context){
		Properties prop = openProperties(context);
		String port = "";
		if(prop!= null){
			port =prop.getProperty("emailPort");
		}
		return port;
		}
	
	public static void setProjectStage(int projectStage){
		FileOutputStream output=null;
		try {
			
			Properties prop =  openProperties(QuartzContextListener.context);
		
			String basepath = VaadinService.getCurrent()
	                  .getBaseDirectory().getAbsolutePath();
			output = new FileOutputStream(basepath+url);
			prop.setProperty("projectStage",Integer.toString(projectStage));
			prop.store(output,null);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			errorLogger.error("Error in ProgramPreferences",e);
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			errorLogger.error("Error in ProgramPreferences",e);
			e.printStackTrace();
		}
		finally {
			if (output != null) {
				try {
					output.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
	}}
	
	public static int getProjectStage(){
		Properties prop = openProperties();
		int projectStage=0;
		if(prop!= null){
			projectStage = Integer.parseInt(prop.getProperty("projectStage"));
		}
		return projectStage;
		}
		
		
	public static int getProjectStage(ServletContext context){
		Properties prop = openProperties(context);
		int projectStage=0;
		if(prop!= null){
			projectStage = Integer.parseInt(prop.getProperty("projectStage"));
		}
		return projectStage;
	}
	
	}





