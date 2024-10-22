package com.github.fiap.coleta_api.model;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Table(name = "notificacoes")
@Getter
@Setter
@EqualsAndHashCode(callSuper = false)
public class Notificacao extends AbstractEntity<Long> {

    @Column(name = "tipo")
    private String tipo;
    @Column(name = "descricao")
    private String descricao;
    @Column(name = "email_destinatario")
    private String emailDestinatario;
    @Column(name = "data_envio")
    private LocalDate dataEnvio;

}
