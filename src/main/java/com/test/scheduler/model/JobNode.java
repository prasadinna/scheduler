package com.test.scheduler.model;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class JobNode {
	protected enum JobStatus {
		  NOTSTARTED,
		  STARTED,
		  ERROR
		}
 
	private String primaryKey;
	private int timeToRun;
	private List<String> parentsKey;
	private List<JobNode> parentNodes = new ArrayList<JobNode>();
	private List<JobNode> childrenNodes = new ArrayList<JobNode>();
	private JobStatus status = JobStatus.NOTSTARTED;
	private int remainingTime = 0;
	private int resourceEnqueTime = -1;
	
	public String getPrimaryKey() {
		return primaryKey;
	}

	public void setPrimaryKey(String primaryKey) {
		this.primaryKey = primaryKey;
	}

	public int getTimeToRun() {
		return timeToRun;
	}

	public void setTimeToRun(int timeToRun) {
		this.timeToRun = timeToRun;
		if(this.status == JobStatus.ERROR) {
			remainingTime = 0;
		}else {
			remainingTime = timeToRun;
		}
		
	}

	public List<String> getParentsKey() {
		return parentsKey;
	}

	public void setParentsKey(List<String> parentsKey) {
		this.parentsKey = parentsKey;
	}

	public List<JobNode> getChildrenNodes() {
		return childrenNodes;
	}

	public void setChildrenNodes(List<JobNode> childrenNodes) {
		this.childrenNodes = childrenNodes;
	}

	public List<JobNode> getParentNodes() {
		return parentNodes;
	}

	public void setParentNodes(List<JobNode> parentNodes) {
		this.parentNodes = parentNodes;
	}

	public boolean hasNoDependency() {
		return parentNodes == null || parentNodes.size() <1;
	}
	
	public void removeParent(JobNode parent) {
		parentNodes.remove(parent);
	}
	@Override
	public String toString() {
		List<String> parents =  parentNodes.stream().map(node-> node.getPrimaryKey()).collect(Collectors.toList());
		List<String> childs =  childrenNodes.stream().map(node-> node.getPrimaryKey()).collect(Collectors.toList());

		return "JobNode [primaryKey=" + primaryKey + ", timeToRun=" + timeToRun 
				+ ", parents:"+parents+", childs:"+childs
				+",remainingTime:"+remainingTime+"]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((primaryKey == null) ? 0 : primaryKey.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		JobNode other = (JobNode) obj;
		if (primaryKey == null) {
			if (other.primaryKey != null)
				return false;
		} else if (!primaryKey.equals(other.primaryKey))
			return false;
		return true;
	}

	public JobStatus getStatus() {
		return status;
	}

	public void setStatus(JobStatus status) {
		this.status = status;
		if(this.status == JobStatus.ERROR) {
			remainingTime = 0;
		}
	}
	
	public void reduceRemainingTime(int reduce) {
		this.remainingTime = this.remainingTime-reduce;
	}
	
	public int getRemainingTime() {
		if(this.status == JobStatus.ERROR) {
			return 0;
		}
		return this.remainingTime;
	}

	public int getResourceEnqueTime() {
		return resourceEnqueTime;
	}

	public void setResourceEnqueTime(int resourceEnqueTime) {
		this.resourceEnqueTime = resourceEnqueTime;
	}
}
