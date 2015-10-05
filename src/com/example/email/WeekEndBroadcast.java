package com.example.email;

import java.util.Date;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.example.programpreferences.ProgramPreferences;



public class WeekEndBroadcast implements Job {

	
		
		  
	

	@Override
	public void execute(JobExecutionContext arg0) throws JobExecutionException {
		   if(ProgramPreferences.getProjectStage()==1 || ProgramPreferences.getProjectStage()==2){
			   EmailManagement.sendAfternoonReminder();
			   
		   }
	}

}
