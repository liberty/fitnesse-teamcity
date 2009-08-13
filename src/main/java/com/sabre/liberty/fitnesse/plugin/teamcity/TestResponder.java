package com.sabre.liberty.fitnesse.plugin.teamcity;

public class TestResponder extends fitnesse.responders.run.TestResponder {
   public TestResponder() {
      formatters.add(new TeamCityLoggingFixtureListener());
   }
}
