package com.test.scheduler.model;

import org.junit.Test;

public class TestSchedulerFactory {

	@Test
	public void testFactoryMEthodReturNotNull() {
		JobScheduler scheduler = SchedulerFactory.getJobScheduler();
		assert(scheduler != null);
	}
}
