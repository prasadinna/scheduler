package com.test.scheduler.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import com.test.scheduler.model.implmentation.JobNodeTimeComparator;
import com.test.scheduler.model.implmentation.ResourceMatcherImpl;

public class JobGraph {
	Map<String,JobNode> keyNodeMap = new HashMap<>();
	private  boolean  addRandomError = false;
	ResourceMatcherImpl resourceMacther;
	private int executionTime = 0;
	
	public static JobGraph buildJobGraphFromDTOWithRandomError(Map<String,JobNodeDTO> jobMap, int resource) {
		JobGraph graph =  new JobGraph(jobMap, true);
		graph.setResourceMacther(getResourceMatcher(resource, graph));
		return graph;
	}
	public static JobGraph buildJobGraphFromDTO(Map<String,JobNodeDTO> jobMap,int resource) {
		JobGraph graph = new JobGraph(jobMap, false);
		graph.setResourceMacther(getResourceMatcher(resource, graph));
		return graph;
	}
	
	private static ResourceMatcherImpl getResourceMatcher(int resourcneNumber, JobGraph graph) {
		return new ResourceMatcherImpl(resourcneNumber,graph);
	}
	
	private JobGraph(Map<String,JobNodeDTO> jobMap, boolean randomError) {
		addRandomError = randomError;
		buildJobNodeMap(jobMap);
		buildParentChildRelation();
	}
	
	public ScheduleSequenceDetails getPossibleSchedule() {
		
		ScheduleSequenceDetails scheduleDetails = new ScheduleSequenceDetails();
		List<JobNode> errorNodes = getErrorNodes();
		if( errorNodes.size() > 0) {
			scheduleDetails.setErrorNodes(errorNodes);
		}
		
		List<JobNode> NodeSequenceList = new ArrayList<JobNode>();
		
		List<JobNode> overFlow = new ArrayList<>();
		
		while(!isEmptyGraph()) {
			List currentList = getJobNodesToEnque();
			List<JobNode> completedNodes = null;
			if(currentList == null || currentList.size() == 0) {
				completedNodes = resourceMacther.getCompletedNodes();
				if(completedNodes == null || completedNodes.size() == 0) {
					break;
				}
				
			}else {
				Collections.sort(currentList, new JobNodeTimeComparator());
				overFlow = resourceMacther.addNodesToQ(currentList);
				completedNodes = resourceMacther.getCompletedNodes();
			}

			
			
			if(completedNodes != null ) {
				completedNodes.forEach(node -> this.removeParentNode(node));
			}
			NodeSequenceList.addAll(completedNodes);
			if(completedNodes != null && completedNodes.size() > 0) {
				executionTime = executionTime+completedNodes.get(0).getRemainingTime();
			}
		}
		
		
		
		if(isEmptyGraph()) {
			List<JobNode> completedNodes = resourceMacther.getCompletedNodes();
			while(completedNodes != null && completedNodes.size() > 0) {
				NodeSequenceList.addAll(completedNodes);
			}
			scheduleDetails.setCyclic(false);
			scheduleDetails.setPath(NodeSequenceList);
			scheduleDetails.setExecutionTime(executionTime);
		}else {
			scheduleDetails.setCyclic(true);
		}
		return scheduleDetails;
	}
	
	private List<JobNode> getJobNodesToEnque(){
		List<JobNode> currentList = getJobNodeWithoutDependency();
		return resourceMacther.removeQdJobs(currentList);
		
	}
	
	public void taskEnqueListener(List<JobNode> taskEnqd) {
		if(taskEnqd != null) {
			taskEnqd.forEach(task ->{
				task.setResourceEnqueTime(this.executionTime);
			});
		}
	}
	
	private List<JobNode> getErrorNodes(){
		List<JobNode> erroNodesList = new ArrayList<>();
		for(Map.Entry<String,JobNode> entry:this.keyNodeMap.entrySet()) {
			JobNode current = entry.getValue();
			if(current.getStatus() == JobNode.JobStatus.ERROR) {
				erroNodesList.add(current);
			}
		}
		return erroNodesList;
	}
	public boolean isEmptyGraph() {
		return keyNodeMap.size() < 1;
	}
	
	public List<JobNode> getJobNodeWithoutDependency(){
		List<JobNode> independentJobNodes = new ArrayList<>();
		for(Map.Entry<String,JobNode> entry:this.keyNodeMap.entrySet()) {
			JobNode current = entry.getValue();
			if(current.hasNoDependency()) {
				independentJobNodes.add(current);
			}
		}
		return independentJobNodes;
	} 
	
	
	
	public boolean removeParentNode(JobNode node) {
		if(keyNodeMap.containsKey(node.getPrimaryKey())) {
			JobNode parentNode = keyNodeMap.get(node.getPrimaryKey());
			removeChildNodeReference(parentNode);
			keyNodeMap.remove(node.getPrimaryKey());
			return true;
		}
		return false;
	}
	
	private void removeChildNodeReference(final JobNode parent) {
		List<JobNode> childNodes = parent.getChildrenNodes();
		childNodes.forEach(node -> node.removeParent(parent));
	}
	
	private void buildJobNodeMap(Map<String, JobNodeDTO> jobMap) {

		String key = null;
		if (addRandomError) {
			Object[] keys =  jobMap.keySet().toArray();
			key = (String)keys[new Random().nextInt(keys.length)];
		}

		for (Map.Entry<String, JobNodeDTO> entry : jobMap.entrySet()) {
			JobNode node = convert2JobnNode(entry.getValue());

			if (addRandomError && key.equals(node.getPrimaryKey())) {
				node.setStatus(JobNode.JobStatus.ERROR);
			}

			keyNodeMap.put(entry.getKey(), node);

		}
	}
	
	private JobNode convert2JobnNode(JobNodeDTO nodeDTO) {
		JobNode node = new JobNode();
		node.setPrimaryKey(nodeDTO.getPrimaryKey());
		node.setTimeToRun(nodeDTO.getTimeToRun());
		node.setParentsKey(nodeDTO.getParentsKey());
		return node;
	}
	
	public void buildParentChildRelation() {
		for(Map.Entry<String,JobNode> entry:this.keyNodeMap.entrySet()) {
			List<String> parentKeys = entry.getValue().getParentsKey();
			if(parentKeys != null) {
				parentKeys.forEach(key ->{
					JobNode parent = keyNodeMap.get(key);
					JobNode child = entry.getValue();
					child.getParentNodes().add(parent);
					parent.getChildrenNodes().add(child);
				});
			}
		}
	}

	
	public JobNode getJobNodeByPrimaryKey(String jobName) {
		return keyNodeMap.get(jobName);
	}
	
	@Override
	public String toString() {
		
		return "JobGraph [keyNodeMap=" + keyNodeMap + "]";
		
	}
	public ResourceMatcherImpl getResourceMacther() {
		return resourceMacther;
	}
	public void setResourceMacther(ResourceMatcherImpl resourceMacther) {
		this.resourceMacther = resourceMacther;
	}
	

	
}
