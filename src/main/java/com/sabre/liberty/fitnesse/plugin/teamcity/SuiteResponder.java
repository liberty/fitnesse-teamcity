package com.sabre.liberty.fitnesse.plugin.teamcity;

public class SuiteResponder extends fitnesse.responders.run.SuiteResponder {
   public SuiteResponder() {
      formatters.add(new TeamCityLoggingFixtureListener());
   }
}
