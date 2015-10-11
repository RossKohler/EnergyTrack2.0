package com.example.email;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.example.energytrack2_0.QuartzContextListener;
import com.example.programpreferences.ProgramPreferences;

public class KitchenTipBroadcast implements Job {

	@Override
	public void execute(JobExecutionContext arg0) throws JobExecutionException {
		
		  int projectStage = ProgramPreferences.getProjectStage(QuartzContextListener.context);
		  
		  if(projectStage ==1 || projectStage ==2){
			  EmailManagement.sendKitchenTipEmail();}	
	}

	
	
	
}
