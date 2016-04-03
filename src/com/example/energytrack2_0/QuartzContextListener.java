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

import com.example.database.DatabaseConnection;
import com.example.database.DatabaseQuery;
import com.example.database.DatabaseUpdate;
import com.example.email.EmailManagement;
import com.example.email.GeneralTipBroadcast;
import com.example.email.GeneralTipBroadcastGroupB;
import com.example.email.KitchenTipBroadcast;
import com.example.email.KitchenTipBroadcastGroupB;
import com.example.email.TemplateMarker;
import com.example.email.WeekEndBroadcast;
import com.example.email.WeekStartBroadcast;
import com.example.programpreferences.ProgramPreferences;

public class QuartzContextListener implements ServletContextListener {
	Logger errorLogger = Logger.getLogger(Log4jContextListener.class);
	public static ServletContext context;
	Scheduler sch;
	Scheduler weekEndSch;
	Scheduler kitchenTipSch;
	Scheduler generalTipSch;
	Scheduler weekStartSch;
	Scheduler generalTipSchGroupB;
	Scheduler kitchenTipSchGroupB;
	
	
	@Override
	public void contextInitialized(ServletContextEvent sce) {
		
		context = sce.getServletContext();
		DatabaseConnection.createConnectionPool();	
		/*JobDetail databaseUpdate = JobBuilder.newJob(DatabaseUpdate.class)
				.withIdentity("DatabaseUpdate").build();
		CronTrigger updateTrigger = TriggerBuilder.newTrigger()
				.withIdentity("updateTrigger").startNow()
				.withSchedule(CronScheduleBuilder.cronSchedule("0 35 * * * ?"))
				.forJob("DatabaseUpdate").build();
		SchedulerFactory databaseFactory = new StdSchedulerFactory();
		try {
			sch = databaseFactory.getScheduler();
			sch.start();
			sch.scheduleJob(databaseUpdate, updateTrigger);
		} catch (SchedulerException ex) {
			ex.printStackTrace();
			errorLogger.error("An Error Occured:", ex);
		}*/
		
		JobDetail weekStartBroadcast = JobBuilder.newJob(WeekStartBroadcast.class)
				.withIdentity("WeekStartBroadcast").build();
		
		CronTrigger weekStartTrigger = TriggerBuilder
				.newTrigger()
				.withIdentity("weekStartTrigger")
				.startNow()
				.withSchedule(
						CronScheduleBuilder.cronSchedule("0 0 10 ? * MON"))
		
				.forJob("WeekStartBroadcast").build();
		
		SchedulerFactory weekStartFactory = new StdSchedulerFactory();
		try {
			weekStartSch = weekStartFactory.getScheduler();
			weekStartSch.start();
			weekStartSch.scheduleJob(weekStartBroadcast, weekStartTrigger);
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
		try {
			weekEndSch = weekEndFactory.getScheduler();
			weekEndSch.start();
			weekEndSch.scheduleJob(weekEndBroadcast, weekEndTrigger);
		} catch (SchedulerException ex) {
			ex.printStackTrace();
			errorLogger.error("An Error Occured:", ex);
		}

		JobDetail kitchenTipBroadcastGroupB = JobBuilder
				.newJob(KitchenTipBroadcastGroupB.class)
				.withIdentity("KitchenTipBroadcastGroupB").build();
		CronTrigger kitchenTipTriggerGroupB = TriggerBuilder
				.newTrigger()
				.withIdentity("KitchenTipBroadcastGroupB")
				.startNow()
				.withSchedule(
						CronScheduleBuilder.cronSchedule("0 0 9 ? * 3#2"))
				
				
				.forJob("KitchenTipBroadcastGroupB").build();

		SchedulerFactory kitchenTipFactoryGroupB = new StdSchedulerFactory();
		try {
			kitchenTipSchGroupB = kitchenTipFactoryGroupB.getScheduler();
			kitchenTipSchGroupB.start();
			kitchenTipSchGroupB.scheduleJob(kitchenTipBroadcastGroupB, kitchenTipTriggerGroupB);
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
		try {
			kitchenTipSch = kitchenTipFactory.getScheduler();
			kitchenTipSch.start();
			kitchenTipSch.scheduleJob(kitchenTipBroadcast, kitchenTipTrigger);
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
						CronScheduleBuilder.cronSchedule("0 0 9 ? * 3#1"))
				.forJob("GeneralTipBroadcast").build();

		SchedulerFactory generalTipFactory = new StdSchedulerFactory();
		try {
			generalTipSch = generalTipFactory.getScheduler();
			generalTipSch.start();
			generalTipSch.scheduleJob(generalTipBroadcast, generalTipTrigger);
		} catch (SchedulerException ex) {
			ex.printStackTrace();
			errorLogger.error("An Error Occured:", ex);
		}
		
		JobDetail generalTipBroadcastGroupB = JobBuilder
				.newJob(GeneralTipBroadcastGroupB.class)
				.withIdentity("GeneralTipBroadcastGroupB").build();
		CronTrigger generalTipTriggerGroupB = TriggerBuilder
				.newTrigger()
				.withIdentity("GeneralTipBroadcastGroupB")
				.startNow()
				.withSchedule(
						CronScheduleBuilder.cronSchedule("0 0 9 ? * 4#1"))
				.forJob("GeneralTipBroadcastGroupB").build();

		SchedulerFactory generalTipFactoryGroupB = new StdSchedulerFactory();
		try {
			generalTipSchGroupB = generalTipFactoryGroupB.getScheduler();
			generalTipSchGroupB.start();
			generalTipSchGroupB.scheduleJob(generalTipBroadcastGroupB, generalTipTriggerGroupB);
		} catch (SchedulerException ex) {
			ex.printStackTrace();
			errorLogger.error("An Error Occured:", ex);
		}


	}

	@Override
	public void contextDestroyed(ServletContextEvent sce) {
		try {
			weekStartSch.shutdown();
			generalTipSch.shutdown();
			kitchenTipSch.shutdown();
			weekEndSch.shutdown();
			sch.shutdown();
			sce = null;
		} catch (SchedulerException e) {
			// TODO Auto-generated catch block
			errorLogger.error(e);
			e.printStackTrace();
		}
		

	}

}
