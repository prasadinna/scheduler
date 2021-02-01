package com.test.scheduler.model;

import com.test.scheduler.model.implmentation.JobSchedulerImpl;

public class SchedulerFactory {
	private static JobScheduler scheduler = new JobSchedulerImpl();
	public static JobScheduler getJobScheduler() {
		return scheduler;
	}

}
