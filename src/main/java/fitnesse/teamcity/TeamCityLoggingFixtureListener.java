// Copied since I don't have access to source of fitnesse-teamcity.
// Modified to support full name of the tests and to output html content of the test on failures.
package fitnesse.teamcity;

import fitnesse.responders.run.formatters.BaseFormatter;
import fitnesse.responders.run.CompositeExecutionLog;
import fitnesse.responders.run.TestSystem;
import fitnesse.responders.run.TestSummary;
import fitnesse.wiki.WikiPage;

public class TeamCityLoggingFixtureListener extends BaseFormatter {
  private String currentTestName;
  private int failedTests;
  private int totalTests;
  private boolean testFailed;
  private StringBuilder testOutputBuffer = new StringBuilder();

  public void setExecutionLogAndTrackingId(String stopResponderId, CompositeExecutionLog log) throws Exception {
  }

  public void announceNumberTestsToRun(int testsToRun) {
  }

  public void testSystemStarted(TestSystem testSystem, String testSystemName, String testRunner) throws Exception {
  }

  public void newTestStarted(WikiPage test, long time) throws Exception {
    currentTestName = test.getPageCrawler().getFullPath(test).toString();
    logTestStarted();
    ++totalTests;
    testFailed = false;
  }

  public void testOutputChunk(String output) throws Exception {
    // TODO Here save output and on errorOccured log it -> need the stacktrace renderer plugin in TC.
    testOutputBuffer.append(output);
  }

  public void testComplete(WikiPage test, TestSummary testSummary) throws Exception {
    if (testSummary.getExceptions() > 0 || testSummary.getWrong() > 0) {
      logTestFailed();
      ++failedTests;
      testFailed = true;
    }
    logTestFinished();
    testOutputBuffer = new StringBuilder();
  }

  public void errorOccured() {
  }

  public void writeHead(String pageType) throws Exception {
  }

  public void allTestingComplete() throws Exception {
  }

  private void logTestStarted() {
    logTeamcityMessage("testStarted name='%s' captureStandardOutput='true'");
  }

  private void logTestFinished() {
    logTeamcityMessage("testFinished name='%s'");
    if (testFailed) {
      logTeamcityMessage(String.format("buildProblem description='%s' identity='%s'", getStatusText(), currentTestName));
      logTeamcityMessage(String.format("buildStatus text='{build.status.text}; %s'", getStatusText()));
    }
    else {
      logTeamcityMessage(String.format("buildStatus text='{build.status.text}; %s'", getStatusText()));
    }
  }

  private String getStatusText() {
    StringBuilder builder = new StringBuilder("Fitnesse ");
    if (failedTests > 0)
      builder.append("failed: ").append(failedTests).append(", ");
    builder.append("passed: ").append(totalTests - failedTests);
    return builder.toString();
  }

  private void logTestFailed() {
    System.out.println(testOutputBuffer.toString());
    logTeamcityMessage("testFailed name='%s'");
  }

  private void logTeamcityMessage(String TEST_FINISHED_MESSAGE) {
    System.out.println(String.format("##teamcity[" + TEST_FINISHED_MESSAGE + "]", currentTestName));
  }

  //##teamcity[buildStatus status='<status value>' text='{build.status.text} and some aftertext']
}
