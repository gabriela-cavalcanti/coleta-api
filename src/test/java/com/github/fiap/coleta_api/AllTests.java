package com.github.fiap.coleta_api;

import com.github.fiap.coleta_api.cucumber.RunCucumberTest;
import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.Suite;
import org.junit.platform.suite.api.SuiteDisplayName;

@Suite
@SelectClasses({
        UnitTests.class,
        RunCucumberTest.class
})
@SuppressWarnings("squid:S2187")
@SuiteDisplayName("Suite que reúne todos os testes de Unidade e de Aceitação")
public class AllTests {

}