package com.sabre.liberty.fitnesse.plugin.teamcity;

import fitnesse.responders.run.CompositeExecutionLog;
import fitnesse.responders.run.TestSummary;
import fitnesse.responders.run.TestSystem;
import fitnesse.responders.run.formatters.BaseFormatter;
import fitnesse.wiki.WikiPage;

public class TeamCityLoggingFixtureListener extends BaseFormatter {
   private String currentTestName;
   private int failedTests;
   private int totalTests;

   public void setExecutionLogAndTrackingId(String stopResponderId, CompositeExecutionLog log) throws Exception {
   }

   public void announceNumberTestsToRun(int testsToRun) {
   }

   public void testSystemStarted(TestSystem testSystem, String testSystemName, String testRunner) throws Exception {
   }

   public void newTestStarted(WikiPage test, long time) throws Exception {
      currentTestName = test.getName();  //Is it the full name?
      logTestStarted();
      ++totalTests;
   }

   public void testOutputChunk(String output) throws Exception {
      // TODO Here save output and on errorOccured log it -> need the stacktrace renderer plugin in TC.
   }

   public void testComplete(WikiPage test, TestSummary testSummary) throws Exception {
      logTestFinished();
   }

   public void errorOccured() {
      logTestFailed();
      ++failedTests;
   }

   public void writeHead(String pageType) throws Exception {
   }

   public void allTestingComplete() throws Exception {
   }

   private void logTestStarted() {
      logTeamcityMessage("testStarted name='%s'");
   }

   private void logTestFinished() {
      logTeamcityMessage("testFinished name='%s'");
      logTeamcityMessage(String.format("buildStatus status='%s' text='{build.status.text}; %s'", getStatus(), getStatusText()));
   }

   private String getStatus() {
      return (failedTests > 0) ? "FAILURE" : "SUCCESS";
   }
   private String getStatusText() {
      StringBuilder builder = new StringBuilder("Fitnesse ");
      if (failedTests > 0)
         builder.append("failed: ").append(failedTests).append(", ");
      builder.append("passed: ").append(totalTests - failedTests);
      return builder.toString();
   }

   private void logTestFailed() {
      logTeamcityMessage("testFailed name='%s'");
   }

   private void logTeamcityMessage(String TEST_FINISHED_MESSAGE) {
      System.out.println(String.format("##teamcity[" + TEST_FINISHED_MESSAGE + "]", currentTestName));
   }

   //##teamcity[buildStatus status='<status value>' text='{build.status.text} and some aftertext']
}
