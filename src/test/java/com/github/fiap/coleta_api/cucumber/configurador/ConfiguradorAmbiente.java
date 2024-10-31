package com.github.fiap.coleta_api.cucumber.configurador;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Log4j2
@Component
@RequiredArgsConstructor
public class ConfiguradorAmbiente {

    private final JdbcTemplate jdbcTemplate;

    public void configurarAmbiente() {
        log.info("INICIO - Inicializando Contexto");
        //TODO - adicionar pre configuracoes para contexto
        log.info("FIM - Inicializando Contexto");
    }
}