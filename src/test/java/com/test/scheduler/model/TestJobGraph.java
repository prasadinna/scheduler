package com.test.scheduler.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import com.test.scheduler.console.controller.MockConsoleInputParser;

public class TestJobGraph {

	public JobGraph createJobGraph() {
		MockConsoleInputParser parser = new MockConsoleInputParser();
		List input = new ArrayList<String>();
		input.add("T4(3):T1,T2,T3");
		input.add("T1(10):");
		input.add("T2(2):");
		input.add("T3(3):"); 
		//input.add("T5(4):");
		//input.add("T6(2):T5"); 

		
		parser.setInput(input);
		Map output = parser.parseNgetJobDTOList();
		JobGraph jobGraph = JobGraph.buildJobGraphFromDTO(output,2);
		return jobGraph;
	}
	
	public JobGraph createJobGraphSimple() {
		MockConsoleInputParser parser = new MockConsoleInputParser();
		List input = new ArrayList<String>();

		
		input.add("T8(1):T4");
		input.add("T4(1):T1");
		input.add("T7(1):T5,T6");
		input.add("T6(1):T5,T3");
		input.add("T5(1):T1,T2");
		input.add("T1(1):");
		input.add("T2(1):");
		input.add("T3(1):");
		
		parser.setInput(input);
		Map output = parser.parseNgetJobDTOList();
		JobGraph jobGraph = JobGraph.buildJobGraphFromDTO(output,2);
		return jobGraph;
	}
	
	public JobGraph createJobGraphWithError() {
		MockConsoleInputParser parser = new MockConsoleInputParser();
		List input = new ArrayList<String>();
		input.add("T8(1):T4");
		input.add("T4(1):T1");
		input.add("T7(1):T5,T6");
		input.add("T6(1):T5,T3");
		input.add("T5(1):T1,T2");
		input.add("T1(1):");
		input.add("T2(1):");
		input.add("T3(1):");
		parser.setInput(input);
		Map output = parser.parseNgetJobDTOList();
		JobGraph jobGraph = JobGraph.buildJobGraphFromDTOWithRandomError(output,2);
		return jobGraph;
	}

	@Test
	public void nonNullJobGraph() {
		JobGraph jobGraph = createJobGraph();
		assert (jobGraph != null);
		assert (!jobGraph.isEmptyGraph());
	}

	@Test
	public void testNodesWithoutDependency() {
		JobGraph jobGraph = createJobGraphSimple();

		List<JobNode> jobNodeList = jobGraph.getJobNodeWithoutDependency();
		assert (jobNodeList != null && jobNodeList.size() == 3);
		assert ("T1 T2 T3".contains(jobNodeList.get(0).getPrimaryKey()));
		assert ("T1 T2 T3".contains(jobNodeList.get(1).getPrimaryKey()));
		assert ("T1 T2 T3".contains(jobNodeList.get(2).getPrimaryKey()));
	}

	@Test
	public void testParentNodeRemoval() {
		JobGraph jobGraph = createJobGraphSimple();
		JobNode nodeT1 = jobGraph.getJobNodeByPrimaryKey("T1");
		JobNode nodeT4 = jobGraph.getJobNodeByPrimaryKey("T4");
		JobNode nodeT5 = jobGraph.getJobNodeByPrimaryKey("T5");

		assert (nodeT4.getParentNodes().contains(nodeT1));
		assert (nodeT5.getParentNodes().contains(nodeT1));

		boolean deleteSuccess = jobGraph.removeParentNode(nodeT1);
		assert (deleteSuccess);

		// check removal of parent and from map
		assert (jobGraph.getJobNodeByPrimaryKey("T1") == null);

		assert (!nodeT4.getParentNodes().contains(nodeT1));
		assert (!nodeT5.getParentNodes().contains(nodeT1));
	}
	
	
	

	@Test
	public void testSequence() {
		System.out.println("-- start Test sequence without error---");
		JobGraph jobGraph = createJobGraph();
		ScheduleSequenceDetails scheduleSequence = jobGraph.getPossibleSchedule();
		assert(!scheduleSequence.isCyclic());
		scheduleSequence.printJobSequenceDetails();
		System.out.println("-- end Test sequence without error---");
	}
	
	
	@Test
	public void testSequenceWithError() {
		System.out.println("-- start Test sequence with error---");
		JobGraph jobGraph = createJobGraphWithError();
		ScheduleSequenceDetails scheduleSequence = jobGraph.getPossibleSchedule();
		assert(scheduleSequence.getErrorNodes().size() > 0);
		scheduleSequence.printJobSequenceDetails();
		System.out.println("-- end Test sequence with error---");
	}
}
