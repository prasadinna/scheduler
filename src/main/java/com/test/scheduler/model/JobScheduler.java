package com.test.scheduler.model;

public interface JobScheduler {
    public boolean initializeJobScheduler(TaskInputParser parser, int resource);
    public boolean initializeJobSchedulerWithError(TaskInputParser parser, int resource);
    public ScheduleSequenceDetails getSchedulePlan();
    public void removeJob(String jobKey);
    
}
