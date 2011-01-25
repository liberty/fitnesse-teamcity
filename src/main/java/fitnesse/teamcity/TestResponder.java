package fitnesse.teamcity;

public class TestResponder extends fitnesse.responders.run.TestResponder {
   public TestResponder() {
      formatters.add(new TeamCityLoggingFixtureListener());
   }

   protected void doSending() throws Exception {
      super.doSending();
      if (request.hasInput("exit"))
         System.exit(formatters.getErrorCount());
   }

}
