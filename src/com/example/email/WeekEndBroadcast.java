package com.example.email;

import java.util.Date;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.example.energytrack2_0.QuartzContextListener;
import com.example.programpreferences.ProgramPreferences;



public class WeekEndBroadcast implements Job {

	
		
		  
	

	@Override
	public void execute(JobExecutionContext arg0) throws JobExecutionException {
		
		   int projectStage = ProgramPreferences.getProjectStage(QuartzContextListener.context);
		   if(projectStage ==1 || projectStage ==2){
			   EmailManagement.sendAfternoonReminder();
			   
		   }
	}

}
