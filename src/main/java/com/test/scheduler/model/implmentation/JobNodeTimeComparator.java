package com.test.scheduler.model.implmentation;

import java.util.Comparator;

import com.test.scheduler.model.JobNode;

public class JobNodeTimeComparator implements Comparator<JobNode> {

	@Override
	public int compare(JobNode first, JobNode second) {
		return first.getRemainingTime() - second.getRemainingTime();
	}

}
