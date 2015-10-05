package com.example.email;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.example.programpreferences.ProgramPreferences;

public class KitchenTipBroadcast implements Job {

	@Override
	public void execute(JobExecutionContext arg0) throws JobExecutionException {
		  if(ProgramPreferences.getProjectStage()==1 || ProgramPreferences.getProjectStage()==2){
			  EmailManagement.sendKitchenTipEmail();}	
	}

	
	
	
}
