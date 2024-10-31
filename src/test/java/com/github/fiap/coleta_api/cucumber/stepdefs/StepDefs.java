package com.github.fiap.coleta_api.cucumber.stepdefs;

import com.github.fiap.coleta_api.cucumber.configurador.ConfiguradorAmbiente;
import org.springframework.beans.factory.annotation.Autowired;

public class StepDefs {

    @Autowired
    private ConfiguradorAmbiente configuradorAmbiente;

    public void inicializarContexto() {
        configuradorAmbiente.configurarAmbiente();
    }
}