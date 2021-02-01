package com.test.scheduler.console.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import com.test.scheduler.model.JobNodeDTO;

public class TestConsoleTaskInputParser {
   @Test
   public void testgetJobGraph() {
	   ConsoleTaskInputParser parser = new ConsoleTaskInputParser();
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
	   Map<String, JobNodeDTO> output = parser.parseNgetJobDTOList();
	   assert(output != null);
	   assert(output.containsKey("T1")||output.containsKey("T2")||output.containsKey("T3")||output.containsKey("T4")||
			   output.containsKey("T5")||output.containsKey("T6")||output.containsKey("T7")||output.containsKey("T8"));
	   JobNodeDTO jobNode1 = output.get("T1");
	   JobNodeDTO jobNode5 = output.get("T5");
	   assert(jobNode1.getPrimaryKey().equals("T1")&& jobNode1.getParentsKey() == null);
	   assert(jobNode5.getPrimaryKey().equals("T5")&& jobNode5.getParentsKey() != null);

   }
}
