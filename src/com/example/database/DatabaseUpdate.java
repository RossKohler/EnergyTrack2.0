package com.example.database;
import java.util.Date;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;



public class DatabaseUpdate implements Job {

	
		
		  
	

	@Override
	public void execute(JobExecutionContext arg0) throws JobExecutionException {
		   DatabaseQuery.setCurrentUsage();
		   DatabaseQuery.set24hourUsage();
	   	   DatabaseQuery.setWeekUsage();
	   	   DatabaseQuery.setMonthUsage();
	   	   DatabaseQuery.setFloorRanks();
	}

}
