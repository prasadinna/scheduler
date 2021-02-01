package com.test.scheduler.console.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.test.scheduler.model.JobNodeDTO;
import com.test.scheduler.model.TaskInputParser;

 class ConsoleTaskInputParser implements TaskInputParser {
     List<String> inputLines = new ArrayList();
     Map<String,JobNodeDTO> keys2REcordMap = new HashMap<String, JobNodeDTO>();
     
	 public Map<String, JobNodeDTO> parseNgetJobDTOList() {
		 convertStringToJobNodeDTO();
		 return keys2REcordMap;
	}
	 
	
	 protected void setInput( List<String> inputLines) {
		 this.inputLines = inputLines;
	 }
	 
	 protected void convertStringToJobNodeDTO(){
		 buildKeyToRecordMap();

	 }
	 
	 public void buildKeyToRecordMap() {
		 if(inputLines != null) {

			 inputLines.forEach(input -> {
				 JobNodeDTO currentRecord = convertLineToRecord(input);
				 keys2REcordMap.put(currentRecord.getPrimaryKey(), currentRecord);
			 });
		 } 
	 }
	 

	 
	 protected JobNodeDTO convertLineToRecord(String input){
		 if(input != null) {
			String[] recordStrings = input.split(":");
			if(recordStrings != null && recordStrings.length > 0) {
				JobNodeDTO nodeDTO = new JobNodeDTO();
				String primaryKey = extractKey(recordStrings[0]);
				nodeDTO.setPrimaryKey(primaryKey);
				int timeToRun = extractTime2Run(recordStrings[0]);
				nodeDTO.setTimeToRun(timeToRun);
				if(recordStrings.length > 1) {
					List<String> parentKeys = convertToPerentKeys(recordStrings[1]);
					nodeDTO.setParentsKey(parentKeys);
				}
				return nodeDTO;
			}
		 }
		 return null;
	 }
	 
	 private String extractKey(String keyNTime) {
		 if(keyNTime.indexOf("(") > 0) {
			 return keyNTime.substring(0,keyNTime.indexOf("("));
		 }else {
			 return keyNTime;
		 }
	 }
	 
	 private int extractTime2Run(String keyNTime) {
		 if(keyNTime.indexOf("(") > 0) {
			 String time2Run =  keyNTime.substring(keyNTime.indexOf("(")+1,keyNTime.length()-1);
				 int value = Integer.parseInt(time2Run);
				 return value;
			 
		 }else {
			 return 0;
		 }
	 }
	 
	 private List<String> convertToPerentKeys(String parentKEys){
		 return Arrays.asList(parentKEys.split(","));
	 }

}

 
