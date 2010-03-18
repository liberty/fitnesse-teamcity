package fitnesse.teamcity;

import fitnesse.responders.run.formatters.XmlFormatter;
import fitnesse.responders.run.TestSummary;
import fitnesse.FitNesseContext;
import fitnesse.wiki.WikiPage;

import java.io.Writer;
import java.io.ByteArrayOutputStream;

import util.XmlWriter;

import static util.XmlUtil.addTextNode;
import static util.XmlUtil.newDocument;

import org.w3c.dom.Element;
import org.w3c.dom.Document;

public class TestResultXmlFormatter extends XmlFormatter {
  Document currentDocumentToWrite;

  public TestResultXmlFormatter(FitNesseContext context, WikiPage page, WriterFactory writerFactory) throws Exception {
    super(context, page, writerFactory);
  }

  @Override
  public void testComplete(WikiPage test, TestSummary testSummary) throws Exception {
    super.testComplete(test, testSummary);
    writeTestResult(test, testSummary);
  }

  private void writeTestResult(WikiPage test, TestSummary testSummary) throws Exception {
    currentDocumentToWrite = createDocument("testResult");
    addTextNode(currentDocumentToWrite, currentDocumentToWrite.getDocumentElement(), "name", test.getName());

    Element countsNode = currentDocumentToWrite.createElement("counts");
    addTextNode(currentDocumentToWrite, countsNode, "right", String.valueOf(testSummary.getRight()));
    addTextNode(currentDocumentToWrite, countsNode, "wrong", String.valueOf(testSummary.getWrong()));
    addTextNode(currentDocumentToWrite, countsNode, "ignores", String.valueOf(testSummary.getIgnores()));
    addTextNode(currentDocumentToWrite, countsNode, "exceptions", String.valueOf(testSummary.getExceptions()));

    currentDocumentToWrite.getDocumentElement().appendChild(countsNode);
    writeResults();
  }

  @Override
  protected void writeResults(Writer writer) throws Exception {
    if (currentDocumentToWrite != null) {
      writer.write(toString(currentDocumentToWrite));
      currentDocumentToWrite = null;
    }
  }

  private static Document createDocument(String rootNodeName) throws Exception {
    Document document = newDocument();
    Element root = document.createElement(rootNodeName);
    document.appendChild(root);
    return document;
  }

  private static String toString(Document document) throws Exception {
    ByteArrayOutputStream output = new ByteArrayOutputStream();
    XmlWriter writer = new XmlWriter(output);
    writer.write(document);
    writer.close();
    return output.toString();
  }

}
