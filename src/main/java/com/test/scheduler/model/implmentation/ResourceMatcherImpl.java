package com.test.scheduler.model.implmentation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.PriorityQueue;

import com.test.scheduler.model.JobGraph;
import com.test.scheduler.model.JobNode;

public class ResourceMatcherImpl {
	private int totalResource = 0;
	private PriorityQueue<JobNode> reqourceQ;
	private JobGraph graph;

	public ResourceMatcherImpl(int numberOfResource, JobGraph graph) {
		this.totalResource = numberOfResource;
		this.reqourceQ = new PriorityQueue<JobNode>(numberOfResource, new JobNodeTimeComparator());
		this.graph = graph;
		this.graph.setResourceMacther(this);
	}

	public List<JobNode> addNodesToQ(List<JobNode> sortedNodes) {
		
		int currentQsize = reqourceQ.size();
		int allowedEntries = totalResource - currentQsize;
		List<JobNode> overFLow = Collections.EMPTY_LIST;
		List<JobNode> tasksEnqued = new ArrayList<>();
		if (allowedEntries > 0) {

			if (allowedEntries >= sortedNodes.size()) {
				reqourceQ.addAll(sortedNodes);
				tasksEnqued = sortedNodes;
				if(graph != null) {
					graph.taskEnqueListener(tasksEnqued);
				}
				return Collections.EMPTY_LIST;
			} else {
				if(reqourceQ.isEmpty()) {
					for(int i = allowedEntries; i > 0; i--) {
						JobNode node = sortedNodes.get(sortedNodes.size()-1);
						tasksEnqued.add(node);
						reqourceQ.add(node);
						sortedNodes.remove(sortedNodes.size()-1);
					}
				}else {

					for (int i = 1; i <= allowedEntries; i++) {
						JobNode nodeWithMinDiff = getInputNodeWithMinDiff(sortedNodes);
						tasksEnqued.add(nodeWithMinDiff);
						reqourceQ.add(nodeWithMinDiff);
						sortedNodes.remove(nodeWithMinDiff);
						
					}
				}
			}
			overFLow = sortedNodes;
		} else {
			overFLow = sortedNodes;
		}
		if(graph != null) {
			graph.taskEnqueListener(tasksEnqued);
		}
		return overFLow;

	}

	public List<JobNode> getCompletedNodes(){
		List<JobNode> completedJob = new ArrayList<>();
		JobNode node = reqourceQ.poll();
		if(node != null) {
			completedJob.add(node);
			removeJobWithSameREmainingTime(completedJob, node.getRemainingTime());
			decreaseReminingJobTime(node.getRemainingTime());
			
		}
		return completedJob;
		
	}
	
	private void removeJobWithSameREmainingTime(List<JobNode> completedJob, int time ) {
		while(reqourceQ.peek() != null && reqourceQ.peek().getRemainingTime() == time) {
			completedJob.add(reqourceQ.poll());
		}
	}
	
	private void decreaseReminingJobTime(int reduceTime) {
		Iterator<JobNode> itr = reqourceQ.iterator();
		while(itr.hasNext()) {
			JobNode node = itr.next();
			node.reduceRemainingTime(reduceTime);
		}
	}
	
	protected JobNode getInputNodeWithMinDiff(List<JobNode> inputSortedNodes) {
		ListIterator<JobNode> lItr = inputSortedNodes.listIterator();
		JobNode minNode = null;
		int minDiff = 0;
		while (lItr.hasNext()) {
			JobNode currentNode = lItr.next();
			if (minNode == null) {
				minNode = currentNode;
				minDiff = minDiff(currentNode);
			} else {
				int currentDiff = minDiff(currentNode);
				if (currentDiff < minDiff) {
					minDiff = currentDiff;
					minNode = currentNode;
				}
			}

		}
		return minNode;

	}

	private int minDiff(JobNode currentNode) {
		Iterator<JobNode> itr = reqourceQ.iterator();
		int minDiff = Integer.MAX_VALUE;
		while (itr.hasNext()) {
			JobNode nodeinQ = itr.next();
			int currentDiff = Math.abs(nodeinQ.getRemainingTime() - currentNode.getRemainingTime());
			minDiff = currentDiff < minDiff ? currentDiff : minDiff;
		}
		return minDiff;
	}

	/*
	 * public Map<String,List<JobNode>> addTaskToResource(List<JobNode>
	 * reverseSortedNodes){ int currentQsize = reqourceQ.size(); int allowedEntries
	 * = totalResource - currentQsize; List<JobNode> overFLow =
	 * Collections.EMPTY_LIST; Map<String, List<JobNode>> addQResult = new
	 * HashMap<>(); if(allowedEntries > 0) { int begineIndex = 0; int endIndex =
	 * allowedEntries < nodes.size()?allowedEntries:nodes.size(); List<JobNode>
	 * nodesList2Add = nodes.subList(begineIndex, endIndex);
	 * nodesList2Add.forEach(node -> { reqourceQ.add(node); });
	 * 
	 * 
	 * if(allowedEntries < nodes.size()) { overFLow = nodes.subList(endIndex,
	 * nodes.size()); addQResult.put("OVERFLOW", overFLow); }
	 * 
	 * JobNode topNode = reqourceQ.poll(); if(topNode != null) { List<JobNode>
	 * executedJobs = new ArrayList<JobNode>(); executedJobs.add(topNode);
	 * while(reqourceQ.peek() != null && reqourceQ.peek().getRemainingTime() <=
	 * topNode.getRemainingTime()) { executedJobs.add(topNode); }
	 * addQResult.put("FINISHED", executedJobs); for(JobNode node:reqourceQ) {
	 * node.reduceRemainingTime(topNode.getRemainingTime()); } }
	 * 
	 * } return addQResult;
	 * 
	 * }
	 */

	public PriorityQueue<JobNode> getReqourceQ() {
		return reqourceQ;
	}

	public void setReqourceQ(PriorityQueue<JobNode> reqourceQ) {
		this.reqourceQ = reqourceQ;
	}

	public List<JobNode> removeQdJobs(List<JobNode> jobList){
		jobList.removeAll(reqourceQ);
		return jobList;
	}
}
