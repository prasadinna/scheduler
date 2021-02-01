package com.test.scheduler.model;

import java.util.List;
import java.util.Map;

public interface TaskInputParser {
	public Map<String, JobNodeDTO> parseNgetJobDTOList();
}
