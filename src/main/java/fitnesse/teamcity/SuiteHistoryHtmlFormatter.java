package fitnesse.teamcity;

import fit.Counts;
import fitnesse.FitNesseContext;
import fitnesse.responders.run.CompositeExecutionLog;
import fitnesse.responders.run.TestSummary;
import fitnesse.responders.run.TestSystem;
import fitnesse.trinidad.SingleTestResult;
import fitnesse.trinidad.TestResult;
import fitnesse.wiki.WikiPage;

import java.io.File;
import java.io.FileWriter;
import java.io.Writer;

public class SuiteHistoryHtmlFormatter extends HistoryHtmlFormatter {
  private String suiteReportFile;
  private String suiteName;
  private TestSummary suiteSummary;
  private StringBuffer contentBuffer;

  public SuiteHistoryHtmlFormatter(FitNesseContext context, final WikiPage page) throws Exception {
    this(context, page, null, null);
  }

  public SuiteHistoryHtmlFormatter(FitNesseContext context, WikiPage page, String reportDir, String reportFile) throws Exception {
    super(context, page, reportDir);
    suiteReportFile = reportFile;
    suiteName = page.getPageCrawler().getFullPath(page).toString();
    suiteSummary = new TestSummary();
    contentBuffer = new StringBuffer("<br/><h3>Test Results</h3>");
  }

  @Override
  public void writeHead(String pageType) throws Exception {
  }

  @Override
  public void allTestingComplete() throws Exception {
    writeResults();
    suiteSummary.clear();
    contentBuffer = new StringBuffer();
  }

  public void setExecutionLogAndTrackingId(String stopResponderId, CompositeExecutionLog log) throws Exception {
  }

  public void testSystemStarted(TestSystem testSystem, String testSystemName, String testRunner) throws Exception {
  }

  public void newTestStarted(WikiPage test, long time) throws Exception {
  }

  public void testOutputChunk(String output) throws Exception {
  }

  public void testComplete(WikiPage testedPage, TestSummary testSummary) throws Exception {
    suiteSummary.tallyPageCounts(testSummary);
    String fullPageName = testedPage.getPageCrawler().getFullPath(testedPage).toString();
    SingleTestResult currentTestResult = new SingleTestResult(createCounts(testSummary), fullPageName, "");
    addTestResult(currentTestResult);
  }

  private void addTestResult(SingleTestResult currentTestResult) {
    Counts resultCounts = currentTestResult.getCounts();
    String pageName = currentTestResult.getName();
    contentBuffer.append("<div class='").append(getAlternatingStyle()).append("'>")
      .append("<span class='test_summary_results ").append(getCssClass(resultCounts)).append("'>")
      .append(resultCounts.toString()).append("</span>")
      .append("<a href='").append(pageName).append(".html' class='test_summary_link'> ")
      .append(pageName).append("</a>")
      .append("</div>");
  }

  private void writeResults() throws Exception {
    Writer writer = getWriter(context);
    writer.write(buildHtml(createSuiteSummaryResult()));
    writer.close();
  }

  private TestResult createSuiteSummaryResult() {
    return new TestResult() {

      public Counts getCounts() {
        return createCounts(suiteSummary);
      }

      public String getName() {
        return suiteName;
      }

      public String getContent() {
        return contentBuffer.toString();
      }
    };
  }

  @SuppressWarnings({"ResultOfMethodCallIgnored"})
  private Writer getWriter(FitNesseContext context) throws Exception {
    File resultDir = getReportDirectory(context);
    String resultFileName = suiteReportFile != null ? suiteReportFile : "fitnesse.html";
    return new FileWriter(new File(resultDir, resultFileName));
  }

  private String getAlternatingStyle() {
    int totalTests = suiteSummary.getRight() + suiteSummary.getWrong()
      + suiteSummary.getExceptions() + suiteSummary.getIgnores();
    return (totalTests % 2 == 0 ? "alternating_row_2" : "alternating_row_1");
  }

}