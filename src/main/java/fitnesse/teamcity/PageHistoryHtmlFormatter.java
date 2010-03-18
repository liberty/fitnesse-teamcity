package fitnesse.teamcity;

import fitnesse.FitNesseContext;
import fitnesse.responders.run.CompositeExecutionLog;
import fitnesse.responders.run.TestSummary;
import fitnesse.responders.run.TestSystem;
import fitnesse.trinidad.SingleTestResult;
import fitnesse.wiki.WikiPage;

import java.io.File;
import java.io.FileWriter;
import java.io.Writer;

public class PageHistoryHtmlFormatter extends HistoryHtmlFormatter {
  private StringBuilder outputBuffer;

  public PageHistoryHtmlFormatter(FitNesseContext context, final WikiPage page) throws Exception {
    super(context, page);
  }

  public PageHistoryHtmlFormatter(FitNesseContext context, final WikiPage page, String historyDirectory) throws Exception {
    super(context, page, historyDirectory);
  }

  public void newTestStarted(WikiPage testedPage, long time) throws Exception {
    appendHtmlToBuffer(getPage().getData().getHeaderPageHtml());
  }

  public void testOutputChunk(String output) throws Exception {
    appendHtmlToBuffer(output);
  }

  private void appendHtmlToBuffer(String output) {
    if (outputBuffer == null)
      outputBuffer = new StringBuilder();
    outputBuffer.append(changeImageSourcePathToBeRelative(output));
  }

  private String changeImageSourcePathToBeRelative(String output) {
    return output.replaceAll("<img src=\"/files/images/", "<img src=\"images/");
  }

  public void testComplete(WikiPage testedPage, TestSummary testSummary) throws Exception {
    writeResults(testedPage, createTestResult(testedPage, testSummary));
    outputBuffer = null;
  }

  @Override
  public void allTestingComplete() throws Exception {
  }

  @Override
  public void writeHead(String pageType) throws Exception {
  }

  public void testSystemStarted(TestSystem testSystem, String testSystemName, String testRunner) throws Exception {
  }

  public void setExecutionLogAndTrackingId(String stopResponderId,
                                           CompositeExecutionLog log) throws Exception {
  }

  private SingleTestResult createTestResult(WikiPage test, TestSummary testSummary) throws Exception {
    return new SingleTestResult(createCounts(testSummary), test.getName(), outputBuffer.toString());
  }

  private void writeResults(WikiPage testedPage, SingleTestResult testResult) throws Exception {
    Writer writer = getWriter(context, testedPage);
    writer.write(buildHtml(testResult));
    writer.close();
  }

  @SuppressWarnings({"ResultOfMethodCallIgnored"})
  private Writer getWriter(FitNesseContext context, WikiPage page) throws Exception {
    File resultDir = getReportDirectory(context);
    String resultFileName = page.getPageCrawler().getFullPath(page).toString() + ".html";
    return new FileWriter(new File(resultDir, resultFileName));
  }

}
