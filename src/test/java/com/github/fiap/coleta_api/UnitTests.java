package com.github.fiap.coleta_api;

import com.github.fiap.coleta_api.controller.NotificacaoControllerTest;
import com.github.fiap.coleta_api.service.NotificacaoServiceTest;
import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.Suite;
import org.junit.platform.suite.api.SuiteDisplayName;

@Suite
@SelectClasses({
        NotificacaoControllerTest.class,
        NotificacaoServiceTest.class
})
@SuppressWarnings("squid:S2187")
@SuiteDisplayName("Suite que reúne todos os testes de Unidade")
public class UnitTests {

}