package com.test.scheduler.model.implmentation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;

import org.junit.Test;

import com.test.scheduler.model.JobNode;

public class TestResourceMatcherImpl {

	private List<JobNode> getFirstNodeList() {
		List<JobNode> orderedList = new ArrayList<>();
		JobNode node1 = new JobNode();
		node1.setPrimaryKey("T10");
		node1.setTimeToRun(10);

		JobNode node2 = new JobNode();
		node2.setPrimaryKey("T20");
		node2.setTimeToRun(20);

		JobNode node3 = new JobNode();
		node3.setPrimaryKey("T30");
		node3.setTimeToRun(30);

		JobNode node4 = new JobNode();
		node4.setPrimaryKey("T40");
		node4.setTimeToRun(40);

		orderedList.add(node1);
		orderedList.add(node2);
		orderedList.add(node3);
		orderedList.add(node4);
		return orderedList;
	}

	@Test
	public void testEmptyAdd() {
		ResourceMatcherImpl resourceMatcher = new ResourceMatcherImpl(3, null);

		List<JobNode> nodes = resourceMatcher.addNodesToQ(getFirstNodeList());
		assert (nodes.size() == 1 && nodes.get(0).getPrimaryKey().equals("T10"));

	}

	@Test
	public void testEmptyAddNTaskRemove() {
		ResourceMatcherImpl resourceMatcher = new ResourceMatcherImpl(3, null);

		resourceMatcher.addNodesToQ(getFirstNodeList());
		List<JobNode> completedNode = resourceMatcher.getCompletedNodes();
		assert (completedNode != null && completedNode.get(0).getPrimaryKey().equals("T20")
				&& completedNode.get(0).getRemainingTime() == 20);
		PriorityQueue<JobNode> q = resourceMatcher.getReqourceQ();
		assert (q.size() == 2);

		Iterator<JobNode> itr = q.iterator();
		JobNode first = itr.next();
		assert (first.getPrimaryKey().equals("T30") && first.getRemainingTime() == 10);
		JobNode secod = itr.next();
		assert (secod.getPrimaryKey().equals("T40") && secod.getRemainingTime() == 20);

		completedNode = resourceMatcher.getCompletedNodes();
		assert (completedNode != null && completedNode.get(0).getPrimaryKey().equals("T30")
				&& completedNode.get(0).getRemainingTime() == 10);
		itr = q.iterator();
		first = itr.next();
		assert (first.getPrimaryKey().equals("T40") && first.getRemainingTime() == 10);

		completedNode = resourceMatcher.getCompletedNodes();
		assert (completedNode != null && completedNode.get(0).getPrimaryKey().equals("T40")
				&& completedNode.get(0).getRemainingTime() == 10);
		itr = q.iterator();

		assert (!itr.hasNext());
	}
	
	
	
	@Test
	public void testOptimization() {
		ResourceMatcherImpl resourceMatcher = new ResourceMatcherImpl(3, null);

		List<JobNode> remainingNodes = resourceMatcher.addNodesToQ(getFirstNodeList());
		List<JobNode> completedNodes = resourceMatcher.getCompletedNodes();
		assert (completedNodes != null && completedNodes.get(0).getPrimaryKey().equals("T20")
				&& completedNodes.get(0).getRemainingTime() == 20);
		PriorityQueue<JobNode> q = resourceMatcher.getReqourceQ();
		assert (q.size() == 2);

		Iterator<JobNode> itr = q.iterator();
		JobNode first = itr.next();
		assert (first.getPrimaryKey().equals("T30") && first.getRemainingTime() == 10);
		JobNode secod = itr.next();
		assert (secod.getPrimaryKey().equals("T40") && secod.getRemainingTime() == 20);

		JobNode node13 = new JobNode();
		node13.setPrimaryKey("T13");
		node13.setTimeToRun(13);
		
		
		JobNode node19 = new JobNode();
		node19.setPrimaryKey("T19");
		node19.setTimeToRun(19);
		
		remainingNodes.add(node19);
		remainingNodes.add(node13);
		
		Collections.sort(remainingNodes, new JobNodeTimeComparator());
		remainingNodes = resourceMatcher.addNodesToQ(remainingNodes);
		
		completedNodes = resourceMatcher.getCompletedNodes();
		
		assert(completedNodes.size() == 2 && 
				(completedNodes.get(0).getPrimaryKey().equals("T30")|| completedNodes.get(0).getPrimaryKey().equals("T10"))
				&&(completedNodes.get(1).getPrimaryKey().equals("T30")|| completedNodes.get(1).getPrimaryKey().equals("T10")));
		assert(completedNodes.get(0).getRemainingTime() == 10 && completedNodes.get(1).getRemainingTime() == 10 );
		assert(resourceMatcher.getReqourceQ().size() == 1 
				&& resourceMatcher.getReqourceQ().peek().getPrimaryKey().equals("T40") 
				&& resourceMatcher.getReqourceQ().peek().getRemainingTime() ==10);
		
		JobNode node1 = new JobNode();
		node1.setPrimaryKey("T1");
		node1.setTimeToRun(1);
		remainingNodes.add(node1);
		Collections.sort(remainingNodes, new JobNodeTimeComparator());
		
		remainingNodes = resourceMatcher.addNodesToQ(remainingNodes);
		assert(remainingNodes.size() == 1 && remainingNodes.get(0).getPrimaryKey().equals("T1"));
		
		completedNodes = resourceMatcher.getCompletedNodes();
		assert(completedNodes.size() == 1 && completedNodes.get(0).getPrimaryKey().equals("T40"));
		
		remainingNodes = resourceMatcher.addNodesToQ(remainingNodes);
		assert(remainingNodes.size() == 0);
		
		completedNodes = resourceMatcher.getCompletedNodes();
		assert(completedNodes.size() == 1 && completedNodes.get(0).getPrimaryKey().equals("T1"));
		completedNodes = resourceMatcher.getCompletedNodes();
		assert(completedNodes.size() == 1 && completedNodes.get(0).getPrimaryKey().equals("T13"));
		completedNodes = resourceMatcher.getCompletedNodes();
		assert(completedNodes.size() == 1 && completedNodes.get(0).getPrimaryKey().equals("T19"));
	
	}
}
