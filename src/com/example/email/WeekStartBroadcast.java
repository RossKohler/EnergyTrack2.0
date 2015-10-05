package com.example.email;
import java.util.Date;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.example.database.DatabaseQuery;
import com.example.programpreferences.ProgramPreferences;


public class WeekStartBroadcast implements Job {

	
		
		  
	

	@Override
	public void execute(JobExecutionContext arg0) throws JobExecutionException {
		if(ProgramPreferences.getProjectStage()==2){
			EmailManagement.sendCompetitionNoAdvocateEmail();
			EmailManagement.sendCompetitionAdvocateEmail();
			DatabaseQuery.randomEnergyAdvocate();
			EmailManagement.energyAdvocateEmail();
			EmailManagement.sendAdvocateNotiEmail();
			}
		
	}

}
