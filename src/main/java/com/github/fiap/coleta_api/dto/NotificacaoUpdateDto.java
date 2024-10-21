package com.github.fiap.coleta_api.dto;

import com.github.fiap.coleta_api.model.Notificacao;
import lombok.Getter;
import lombok.Setter;
import org.modelmapper.ModelMapper;

import java.time.LocalDate;

@Getter
@Setter
public class NotificacaoUpdateDto {
    private static final ModelMapper mapper = new ModelMapper();

    private long id;
    private String tipo;
    private String descricao;
    private String emailDestinatario;
    private LocalDate dataEnvio;

    public Notificacao toModel() {
        return mapper.map(this, Notificacao.class);
    }
}
