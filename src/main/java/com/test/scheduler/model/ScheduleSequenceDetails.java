package com.test.scheduler.model;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import com.test.scheduler.model.JobNode.JobStatus;

public class ScheduleSequenceDetails {
	
	private List<JobNode> path;
	private boolean isCyclic;
	private List<JobNode> errorNodes;
	private int executionTime = -1;

	
	public List<JobNode> getPath() {
		return path;
	}
	public void setPath(List<JobNode> path) {
		this.path = path;
	}
	public boolean isCyclic() {
		return isCyclic;
	}
	public void setCyclic(boolean isCyclic) {
		this.isCyclic = isCyclic;
	}
	
    public String getExecutionPath() {
    	List<String> sequence = path.stream().
    			map(this::getNodeDetails).
    			collect(Collectors.toList());
    	return sequence.toString();
    }
    
    private String getNodeDetails(JobNode node) {
    	StringBuffer jobDetails = new StringBuffer("\n {"+node.getPrimaryKey());
    	jobDetails.append(", started at:"+node.getResourceEnqueTime());
    	if(node.getStatus() == JobStatus.ERROR){
    		jobDetails.append(",has error, time to run is zeror");
    	}
    	jobDetails.append("}");
    	return jobDetails.toString();
    }
    
    public List<String> getErroresJobKeys(){
    	
    	if(errorNodes != null) {
    		return errorNodes.stream().map(node -> node.getPrimaryKey()).collect(Collectors.toList());
    	}else {
    		return Collections.EMPTY_LIST;
    	}
    }

	public void printJobSequenceDetails() {
		if(errorNodes != null && errorNodes.size() > 0) {
			System.out.println("Some of the jobs have error:"+getErroresJobKeys());
		}
		if(isCyclic) {
			System.out.println("Error:Cyclic dependency in the grap cannot schedule...");
		}else {
			System.out.println("Job Can be scheduled as:"+getExecutionPath());
			System.out.println("TotalExecution Time:"+getExecutionTime());
		}
		
		
	}
	public List<JobNode> getErrorNodes() {
		return errorNodes;
	}
	public void setErrorNodes(List<JobNode> errorNodes) {
		this.errorNodes = errorNodes;
	}
	public int getExecutionTime() {
		return executionTime;
	}
	public void setExecutionTime(int executionTime) {
		this.executionTime = executionTime;
	}
}
