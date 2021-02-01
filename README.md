This is a module to schedule the job with resource constraint
The main component are 
1) JobGraph - mantaint the parsed job configuration. Uses the Kahns breadth first algorithm to get the nodes for the directed ascyclic graph
2) Resource Matcher - responsible to schedule the task on resource and optimize the input task
3) Client - SchedulerConsoleClient to recieve input from the console
4) Input parser - to parse the input data

Test coverage
All the business logics are covered by the test cases. Please refer the following classes, thes can be used in place of console 
  1) TestConsoleTaskInputParser
  2) TestJobGraph
  3) TestResourceMatcherImpl
