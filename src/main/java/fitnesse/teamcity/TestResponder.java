package fitnesse.teamcity;

public class TestResponder extends fitnesse.responders.run.TestResponder {
   public TestResponder() {
      formatters.add(new TeamCityLoggingFixtureListener());
   }
}
