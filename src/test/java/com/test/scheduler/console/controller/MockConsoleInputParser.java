package com.test.scheduler.console.controller;

import java.util.List;

public class MockConsoleInputParser extends ConsoleTaskInputParser {
	 //added for testing
	 public void setInput( List<String> inputLines) {
		 super.setInput(inputLines);
	 }
}
