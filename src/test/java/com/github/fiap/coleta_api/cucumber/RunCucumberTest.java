package com.github.fiap.coleta_api.cucumber;

import io.cucumber.junit.CucumberOptions;
import org.junit.platform.suite.api.IncludeEngines;
import org.junit.platform.suite.api.SelectClasspathResource;
import org.junit.platform.suite.api.Suite;
import org.junit.platform.suite.api.SuiteDisplayName;

@Suite
@IncludeEngines("cucumber")
@SuppressWarnings("squid:S2187")
@SelectClasspathResource("features")
@SuiteDisplayName("Suite que reúne todos os testes de Aceitação com Cucumber")
@CucumberOptions(plugin = {"pretty", "json:target/cucumber.json"}, features = "src/test/resources/features")
public class RunCucumberTest {

}