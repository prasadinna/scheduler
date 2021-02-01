package com.test.scheduler.model.implmentation;

import java.util.Map;

import com.test.scheduler.model.JobGraph;
import com.test.scheduler.model.JobNode;
import com.test.scheduler.model.JobNodeDTO;
import com.test.scheduler.model.JobScheduler;
import com.test.scheduler.model.ScheduleSequenceDetails;
import com.test.scheduler.model.TaskInputParser;

public class JobSchedulerImpl implements JobScheduler {
	
	private JobGraph jobGraph;
	
	@Override
	public boolean initializeJobScheduler(TaskInputParser parser, int resource) {
		Map<String, JobNodeDTO> keyVsJobDtoMap = parser.parseNgetJobDTOList();
		jobGraph = JobGraph.buildJobGraphFromDTO(keyVsJobDtoMap,resource);
		return true;
		
	}
	
	@Override
	public boolean initializeJobSchedulerWithError(TaskInputParser parser, int resource) {
		Map<String, JobNodeDTO> keyVsJobDtoMap = parser.parseNgetJobDTOList();
		jobGraph = JobGraph.buildJobGraphFromDTOWithRandomError(keyVsJobDtoMap,resource);
		return true;
		
	}

	@Override
	public ScheduleSequenceDetails getSchedulePlan() {
		ScheduleSequenceDetails jobSequence = jobGraph.getPossibleSchedule();
		return jobSequence;
	}

	@Override
	public void removeJob(String jobKey) {
		JobNode node =jobGraph.getJobNodeByPrimaryKey(jobKey);
		jobGraph.removeParentNode(node);
		
	}

}
