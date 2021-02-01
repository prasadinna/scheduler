package com.test.scheduler.console.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.test.scheduler.model.JobScheduler;
import com.test.scheduler.model.ScheduleSequenceDetails;
import com.test.scheduler.model.SchedulerFactory;

public class SchedulerConsoleClient {
	
	public static void main(String args[]) {
		int resource = getInputResource();
		boolean randonTaskError = addRandomError();
		List<String> input = collectinputData();
		ConsoleTaskInputParser parser = new ConsoleTaskInputParser();
		parser.setInput(input);
		JobScheduler scheduler = SchedulerFactory.getJobScheduler();;
		if(randonTaskError) {
			scheduler.initializeJobSchedulerWithError(parser, resource);
		}else {
			scheduler.initializeJobScheduler(parser, resource);
		}
		
		scheduler.initializeJobScheduler(parser, resource);
		ScheduleSequenceDetails scheduleSequence = scheduler.getSchedulePlan();
		scheduleSequence.printJobSequenceDetails();


		System.out.println("");
	}
	
	public static boolean addRandomError() {
		Scanner scan = new Scanner(System.in);
		System.out.print("Do you want to intdroduce random task error(y/): ");
		String randomError = scan.nextLine();
		return randomError.trim().equalsIgnoreCase("y");
	}
	public static int getInputResource() {
		Scanner scan = new Scanner(System.in);
		System.out.print("Enter the number of resource: ");
		int num = scan.nextInt();
		return num;
	}
   
   private static List<String> collectinputData(){
	   System.out.println("enter task data, when finished enter empty line");
	   List<String> input = new ArrayList<>();
	   Scanner scanner = new Scanner(System.in);
	   String userData = null;
	   do {
		   userData = scanner.nextLine();
		   if(!userData.trim().equals("")) {
			   if(isValidInput(userData)) {
				   input.add(userData); 
			   }else {
				   System.out.println("Invalid input:"+userData);
			   }
			   input.add(userData);
		   }
	   }while(!userData.trim().equals(""));
	   return input;
   }
   
   private static String regex = "^.*\\(\\d+\\):.*$";
   private static Pattern pattern = Pattern.compile(regex);
   private static boolean isValidInput(String input) {
	   Matcher matcher = pattern.matcher(input);
	   if(!matcher.find()) return false;
	   return true;
   }
}
