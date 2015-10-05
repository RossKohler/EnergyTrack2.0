package com.example.programpreferences;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.apache.log4j.Logger;

import com.example.energytrack2_0.Log4jContextListener;
import com.vaadin.server.VaadinService;



public class ProgramPreferences{
	
	final static Logger errorLogger = Logger.getLogger(Log4jContextListener.class);
	private static FileInputStream input;
	private static Properties prop;
	private static String url = "/WEB-INF/Resources/config.properties";
	
	public static void openProperties(){
		
		prop = new Properties();
		try {
			
			String basepath = VaadinService.getCurrent()
	                  .getBaseDirectory().getAbsolutePath();
			input = new FileInputStream(basepath+url);
			prop.load(input);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			errorLogger.error("Error in ProgramPreferences",e);
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			errorLogger.error("Error in ProgramPreferences",e);
			e.printStackTrace();
		}
	
		}
	
	public static void closeProperties(){
		if(input != null){
			try{
				input.close();
			}
			catch(IOException e){
				errorLogger.error("Error in ProgramPreferences",e);
				e.printStackTrace();
				
			}
		}
			
			
		}
	
	
	public static String getDBUser(){
		openProperties();
		String dbUser = null;
		if(prop!= null && input != null){
			dbUser=prop.getProperty("dbUser");
		
		}
		closeProperties();
		return dbUser;
		}
	
	
	public static String getDBURL(){
		openProperties();
		String dbURL = null;
		if(prop!= null && input != null){
			dbURL=prop.getProperty("dbURL");
			
			
		}
		closeProperties();
		return dbURL;
		}
	
	public static String getDBPass(){
		openProperties();
		String dbPass = null;
		if(prop!= null && input != null){
			dbPass=prop.getProperty("dbPass");
		
		}
		closeProperties();
		return dbPass;
		}
	
	public static String getEmailAddress(){
		openProperties();
		String emailAddress = null;
		if(prop!= null && input != null){
			emailAddress=prop.getProperty("emailAddress");
		
		}
		closeProperties();
		return emailAddress;
		}
	
	public static String getEmailUser(){
		openProperties();
		String emailUser = null;
		if(prop!= null && input != null){
			emailUser=prop.getProperty("emailUser");
		
		}
		closeProperties();
		return emailUser;
		}
	
	public static String getEmailPass(){
		openProperties();
		String emailPass = null;
		if(prop!= null && input != null){
			emailPass=prop.getProperty("emailPass");
		
		}
		closeProperties();
		return emailPass;
		}
	public static String getHost(){
		openProperties();
		String hostname = null;
		if(prop!= null && input != null){
			hostname=prop.getProperty("emailHost");
		
		}
		closeProperties();
		return hostname;
		}
	
	public static String getPort(){
		openProperties();
		String port = null;
		if(prop!= null && input != null){
			port =prop.getProperty("emailPort");
		
		}
		closeProperties();
		return port;
		}
	
	public static void setProjectStage(int projectStage){
		FileOutputStream output=null;
		try {
			if(prop == null){
				prop = new Properties();
			}
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
		openProperties();
		int projectStage=0;
		if(prop!= null && input != null){
			projectStage = Integer.parseInt(prop.getProperty("projectStage"));
		}
		closeProperties();
		return projectStage;
		}
		
		
	}





