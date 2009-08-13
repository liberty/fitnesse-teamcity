package fitnesse.teamcity;

public class SuiteResponder extends fitnesse.responders.run.SuiteResponder {
   public SuiteResponder() {
      formatters.add(new TeamCityLoggingFixtureListener());
   }
}
