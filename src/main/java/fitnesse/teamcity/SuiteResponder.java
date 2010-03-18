package fitnesse.teamcity;

import fitnesse.responders.run.formatters.XmlFormatter;
import fitnesse.responders.run.TestSummary;
import fitnesse.FitNesseContext;
import fitnesse.wiki.WikiPage;

import java.io.Writer;
import java.io.IOException;

public class SuiteResponder extends fitnesse.responders.run.SuiteResponder {
  public SuiteResponder() {
    formatters.add(new TeamCityLoggingFixtureListener());

  }

  @Override
  protected void createFormatterAndWriteHead() throws Exception {
    formatters.add(new TestResultXmlFormatter(context, page, createWriterFactory()));
    addTestHistoryFormatter();
  }

  @Override
  protected void addTestHistoryFormatter() throws Exception {
    String reportDir = (String) request.getInput("reportDir");
    String reportFile = (String) request.getInput("reportFile");
    formatters.add(new PageHistoryHtmlFormatter(context, page, reportDir));
    formatters.add(new SuiteHistoryHtmlFormatter(context, page, reportDir, reportFile));
  }


  private XmlFormatter.WriterFactory createWriterFactory() {
    return new XmlFormatter.WriterFactory() {
      public Writer getWriter(FitNesseContext context, WikiPage page, TestSummary counts, long time) {
        return new Writer() {
          @Override
          public void write(String str) throws IOException {
            try {
              addToResponse(str);
            } catch (Exception e) {
              throw new RuntimeException(e);
            }
          }

          @Override
          public void write(char[] cbuf, int off, int len) throws IOException {
            String fragment = new String(cbuf, off, len);
            try {
              addToResponse(fragment);
            } catch (Exception e) {
              throw new RuntimeException(e);
            }
          }

          @Override
          public void flush() throws IOException {
          }

          @Override
          public void close() throws IOException {
          }
        };
      }
    };
  }


}
