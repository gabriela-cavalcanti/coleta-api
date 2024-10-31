package com.github.fiap.coleta_api.cucumber;

import com.github.fiap.coleta_api.Application;
import com.github.fiap.coleta_api.cucumber.stepdefs.StepDefs;
import com.github.fiap.coleta_api.service.NotificacaoService;
import io.cucumber.java.Before;
import io.cucumber.spring.CucumberContextConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.web.WebAppConfiguration;

@WebAppConfiguration
@ActiveProfiles("test")
@CucumberContextConfiguration
@AutoConfigureMockMvc(printOnlyOnFailure = false)
@SpringBootTest(classes = Application.class)
@ContextConfiguration(classes = {Application.class})
public class SpringContextStepDefs extends StepDefs {

    @MockBean
    private NotificacaoService notificacaoService;

    @Before
    public void inicializarContexto() {
        super.inicializarContexto();
    }
}