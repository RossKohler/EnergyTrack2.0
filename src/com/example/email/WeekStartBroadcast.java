package com.example.email;
import java.util.Date;

import javax.servlet.ServletContext;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.example.database.DatabaseQuery;
import com.example.energytrack2_0.QuartzContextListener;
import com.example.programpreferences.ProgramPreferences;


public class WeekStartBroadcast implements Job {

	
	public static ServletContext context;
		  
	

	@Override
	public void execute(JobExecutionContext arg0) throws JobExecutionException {
		int projectStage = ProgramPreferences.getProjectStage(QuartzContextListener.context);
		if(projectStage==3){
			   //DatabaseQuery.setCurrentUsage();
			   //DatabaseQuery.set24hourUsage();
		   	   //DatabaseQuery.setWeekUsage();
		   	   //DatabaseQuery.setMonthUsage();
		   	   //DatabaseQuery.setFloorRanks();
		   	DatabaseQuery.setExcelWeekUsage();
			EmailManagement.sendCompetitionNoAdvocateEmail();
			EmailManagement.sendCompetitionAdvocateEmail();
			DatabaseQuery.randomEnergyAdvocate();
			EmailManagement.energyAdvocateEmail();
			EmailManagement.sendAdvocateNotiEmail();
			}
		
	}

}
