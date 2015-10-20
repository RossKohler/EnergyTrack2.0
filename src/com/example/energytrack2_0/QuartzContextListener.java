package com.example.energytrack2_0;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.apache.log4j.Logger;
import org.quartz.CronScheduleBuilder;
import org.quartz.CronTrigger;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SchedulerFactory;
import org.quartz.TriggerBuilder;
import org.quartz.impl.StdSchedulerFactory;

import com.example.database.DatabaseQuery;
import com.example.database.DatabaseUpdate;
import com.example.email.EmailManagement;
import com.example.email.GeneralTipBroadcast;
import com.example.email.KitchenTipBroadcast;
import com.example.email.TemplateMarker;
import com.example.email.WeekEndBroadcast;
import com.example.email.WeekStartBroadcast;
import com.example.programpreferences.ProgramPreferences;

public class QuartzContextListener implements ServletContextListener {
	Logger errorLogger = Logger.getLogger(Log4jContextListener.class);
	public static ServletContext context;
	@Override
	public void contextInitialized(ServletContextEvent sce) {
		
		context = sce.getServletContext();
		
		JobDetail databaseUpdate = JobBuilder.newJob(DatabaseUpdate.class)
				.withIdentity("DatabaseUpdate").build();
		CronTrigger updateTrigger = TriggerBuilder.newTrigger()
				.withIdentity("updateTrigger").startNow()
				.withSchedule(CronScheduleBuilder.cronSchedule("0 35 * * * ?"))
				.forJob("DatabaseUpdate").build();
		SchedulerFactory databaseFactory = new StdSchedulerFactory();
		Scheduler sch;
		try {
			sch = databaseFactory.getScheduler();
			sch.start();
			sch.scheduleJob(databaseUpdate, updateTrigger);
		} catch (SchedulerException ex) {
			ex.printStackTrace();
			errorLogger.error("An Error Occured:", ex);
		}

		JobDetail weekEndBroadcast = JobBuilder.newJob(WeekEndBroadcast.class)
				.withIdentity("WeekEndBroadcast").build();
		
		CronTrigger weekEndTrigger = TriggerBuilder
				.newTrigger()
				.withIdentity("weekEndTrigger")
				.startNow()
				.withSchedule(
						CronScheduleBuilder.cronSchedule("0 30 15 ? * FRI"))
				.forJob("WeekEndBroadcast").build();
			
		SchedulerFactory weekEndFactory = new StdSchedulerFactory();
		Scheduler weekEndSch;
		try {
			weekEndSch = weekEndFactory.getScheduler();
			weekEndSch.start();
			weekEndSch.scheduleJob(weekEndBroadcast, weekEndTrigger);
		} catch (SchedulerException ex) {
			ex.printStackTrace();
			errorLogger.error("An Error Occured:", ex);
		}

		JobDetail kitchenTipBroadcast = JobBuilder
				.newJob(KitchenTipBroadcast.class)
				.withIdentity("KitchenTipBroadcast").build();
		CronTrigger kitchenTipTrigger = TriggerBuilder
				.newTrigger()
				.withIdentity("KitchenTipBroadcast")
				.startNow()
				.withSchedule(
						CronScheduleBuilder.cronSchedule("0 0 9 ? * 4#3"))
				.forJob("KitchenTipBroadcast").build();

		SchedulerFactory kitchenTipFactory = new StdSchedulerFactory();
		Scheduler kitcheTipSch;
		try {
			kitcheTipSch = kitchenTipFactory.getScheduler();
			kitcheTipSch.start();
			kitcheTipSch.scheduleJob(kitchenTipBroadcast, kitchenTipTrigger);
		} catch (SchedulerException ex) {
			ex.printStackTrace();
			errorLogger.error("An Error Occured:", ex);
		}
		
		JobDetail generalTipBroadcast = JobBuilder
				.newJob(GeneralTipBroadcast.class)
				.withIdentity("GeneralTipBroadcast").build();
		CronTrigger generalTipTrigger = TriggerBuilder
				.newTrigger()
				.withIdentity("GeneralTipBroadcast")
				.startNow()
				.withSchedule(
						CronScheduleBuilder.cronSchedule("0 0 9 ? * 2#1"))
				.forJob("GeneralTipBroadcast").build();

		SchedulerFactory generalTipFactory = new StdSchedulerFactory();
		Scheduler generalTipSch;
		try {
			generalTipSch = generalTipFactory.getScheduler();
			generalTipSch.start();
			generalTipSch.scheduleJob(generalTipBroadcast, generalTipTrigger);
		} catch (SchedulerException ex) {
			ex.printStackTrace();
			errorLogger.error("An Error Occured:", ex);
		}


	}

	@Override
	public void contextDestroyed(ServletContextEvent sce) {
		// TODO Auto-generated method stub

	}

}
