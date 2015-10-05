package com.example.energytrack2_0;

import java.io.File;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import com.example.email.TemplateMarker;

@WebListener("application context listener")
public class Log4jContextListener implements ServletContextListener {

	@Override
	public void contextInitialized(ServletContextEvent sce) {
		// TODO Auto-generated method stub
		
		ServletContext context = sce.getServletContext();
		System.setProperty("appRootPath", context.getRealPath("/"));
		String log4jConfigFile = context.getInitParameter("log4j-config-location");
		if(log4jConfigFile!=null){
			String fullPath = context.getRealPath("")+File.separator+log4jConfigFile;
			PropertyConfigurator.configure(fullPath);
			System.out.println(fullPath);
			System.out.println("Log4j has been successfully intialised in the Application.");
		}
		else{
			System.out.println("Log4J Failed to configure for this Application.");
		}
		
		
		
		
		
	}

	@Override
	public void contextDestroyed(ServletContextEvent sce) {
		// TODO Auto-generated method stub
		
	}
	
	
	
	
	

}
