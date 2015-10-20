package com.example.email;

import javax.servlet.ServletContext;

import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.example.energytrack2_0.QuartzContextListener;
import com.example.programpreferences.ProgramPreferences;
import com.vaadin.server.VaadinServlet;




public class GeneralTipBroadcast implements Job {
	public static ServletContext context;
	
	
	
	@Override
	public void execute(JobExecutionContext jobContext) throws JobExecutionException {
		//JobDataMap dataMap = jobContext.getJobDetail().getJobDataMap();
		//context = (ServletContext) dataMap.get("context");
		int projectStage = ProgramPreferences.getProjectStage(QuartzContextListener.context);
		if(projectStage ==2 || projectStage ==3){
			EmailManagement.sendEmployeeTipEmail();}
		
	}

}
