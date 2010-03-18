package fitnesse.teamcity;

import fit.Counts;
import fitnesse.trinidad.TestResult;
import fitnesse.responders.run.formatters.BaseFormatter;
import fitnesse.responders.run.TestSummary;
import fitnesse.FitNesseContext;
import fitnesse.wiki.WikiPage;

import java.io.File;

public abstract class HistoryHtmlFormatter extends BaseFormatter {
  private String reportDirectory;

  public HistoryHtmlFormatter(FitNesseContext context, final WikiPage page) throws Exception {
    super(context, page);
  }

  public HistoryHtmlFormatter(FitNesseContext context, final WikiPage page, String reportDirectory) throws Exception {
    super(context, page);
    this.reportDirectory = reportDirectory;
  }

  public String buildHtml(TestResult testResult) {
    StringBuffer content = new StringBuffer();
    content.append("<html>")
      .append(buildHeadHtml(testResult))
      .append(buildBodyHtml(testResult))
      .append("</html>");
    return content.toString();
  }

  protected String buildHeadHtml(TestResult testResult) {
    StringBuffer head = new StringBuffer();
    head.append("<head>")
      .append("<meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\"/>")
      .append("<title>").append(testResult.getName()).append("</title>")
      .append("<link rel='stylesheet' type='text/css' href='css/fitnesse.css' media='screen'/>")
      .append("<link rel='stylesheet' type='text/css' href='css/fitnesse_print.css' media='print'/>")
      .append("<script src=\"javascript/fitnesse.js\" type=\"text/javascript\"></script>")
      .append("</head>");
    return head.toString();
  }

  protected String buildBodyHtml(TestResult testResult) {
    StringBuffer body = new StringBuffer();
    body.append("<body>")
      .append("<h2>").append(testResult.getName()).append("</h2>")
      .append(buildCountsHtml(testResult.getCounts()))
      .append(buildContentHtml(testResult))
      .append("</body>");
    return body.toString();
  }

  protected String buildContentHtml(TestResult testResult) {
    return testResult.getContent();
  }

  protected String buildCountsHtml(Counts resultCounts) {
    StringBuffer counts = new StringBuffer();
    counts.append("<table>")
      .append("<tr><td>Right</td><td>Wrong</td><td>Ignored</td><td>Exceptions</td></tr>")
      .append("<tr class='").append(getCssClass(resultCounts)).append("'>")
      .append("<td>").append(resultCounts.right).append("</td>")
      .append("<td>").append(resultCounts.wrong).append("</td>")
      .append("<td>").append(resultCounts.ignores).append("</td>")
      .append("<td>").append(resultCounts.exceptions).append("</td>")
      .append("</tr>")
      .append("</table>");
    return counts.toString();
  }

  protected Counts createCounts(TestSummary summary) {
    return new Counts(summary.getRight(), summary.getWrong(), summary.getIgnores(), summary.getExceptions());
  }

  protected String getCssClass(Counts c) {
    if (c.wrong > 0) return "fail";
    if (c.exceptions > 0) return "error";
    if (c.right > 0) return "pass";
    return "plain";
  }

  @SuppressWarnings({"ResultOfMethodCallIgnored"})
  protected File getReportDirectory(FitNesseContext context) {
    File reportDir;
    if (reportDirectory != null)
      reportDir = new File(reportDirectory);
    else
      reportDir = context.getTestHistoryDirectory();
    reportDir.mkdirs();
    return reportDir;
  }

}
