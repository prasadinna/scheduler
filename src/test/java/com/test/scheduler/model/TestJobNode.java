package com.test.scheduler.model;

import org.junit.Test;

public class TestJobNode {
	@Test
	public void testDependency(){
		JobNode node = new JobNode();
		assert(node.hasNoDependency());
		
		node.getParentNodes().add(new JobNode());
		assert(!node.hasNoDependency());
	}
}
