package com.test.scheduler.model;

import java.util.List;

public class JobNodeDTO {
	private String primaryKey;
	private int timeToRun;
	private List<String> parentsKey;
	
	private List<JobNodeDTO> childrenKeys;
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
	}
	public List<String> getParentsKey() {
		return parentsKey;
	}
	public void setParentsKey(List<String> parentsKey) {
		this.parentsKey = parentsKey;
	}
	public List<JobNodeDTO> getChildrenKeys() {
		return childrenKeys;
	}
	public void setChildrenKeys(List<JobNodeDTO> childrenKeys) {
		this.childrenKeys = childrenKeys;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((parentsKey == null) ? 0 : parentsKey.hashCode());
		result = prime * result + ((primaryKey == null) ? 0 : primaryKey.hashCode());
		result = prime * result + timeToRun;
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
		JobNodeDTO other = (JobNodeDTO) obj;
		if (parentsKey == null) {
			if (other.parentsKey != null)
				return false;
		} else if (!parentsKey.equals(other.parentsKey))
			return false;
		if (primaryKey == null) {
			if (other.primaryKey != null)
				return false;
		} else if (!primaryKey.equals(other.primaryKey))
			return false;
		if (timeToRun != other.timeToRun)
			return false;
		return true;
	}
	@Override
	public String toString() {
		return "JobNodeDTO [primaryKey=" + primaryKey + ", timeToRun=" + timeToRun + ", parentsKey=" + parentsKey
				+ ", childrenKeys=" + childrenKeys + "]";
	}
	
}
