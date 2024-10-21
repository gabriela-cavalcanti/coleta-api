package com.github.fiap.coleta_api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.fiap.coleta_api.dto.NotificacaoCreateDto;
import com.github.fiap.coleta_api.dto.NotificacaoUpdateDto;
import com.github.fiap.coleta_api.model.Notificacao;
import com.github.fiap.coleta_api.service.NotificacaoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(NotificacaoController.class)
public class NotificacaoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private NotificacaoService notificacaoService;

    @Autowired
    private ObjectMapper objectMapper;

    private Notificacao notificacao;
    private NotificacaoCreateDto notificacaoCreateDto;
    private NotificacaoUpdateDto notificacaoUpdateDto;

    @BeforeEach
    public void setup() {
        notificacao = new Notificacao();
        notificacao.setId(1L);
        notificacao.setTipo("TIPO_TESTE");
        notificacao.setDescricao("DESCRICAO_TESTE");
        notificacao.setEmailDestinatario("EMAIL_TESTE");
        notificacao.setDataEnvio(LocalDate.of(2024, 10, 21));

        notificacaoCreateDto = new NotificacaoCreateDto();
        notificacaoCreateDto.setTipo("TIPO_TESTE");
        notificacaoCreateDto.setDescricao("DESCRICAO_TESTE");
        notificacaoCreateDto.setEmailDestinatario("EMAIL_TESTE");
        notificacaoCreateDto.setDataEnvio(LocalDate.of(2024, 10, 21));

        notificacaoUpdateDto = new NotificacaoUpdateDto();
        notificacaoUpdateDto.setId(1L);
        notificacaoUpdateDto.setTipo("TIPO_TESTE_UPDATED");
        notificacaoUpdateDto.setDescricao("DESCRICAO_TESTE_UPDATED");
        notificacaoUpdateDto.setEmailDestinatario("EMAIL_TESTE_UPDATED");
        notificacaoUpdateDto.setDataEnvio(LocalDate.of(2024, 12, 31));
    }

    @Test
    @DisplayName("Should return list of notifications when GET /notificacoes is called")
    public void shouldReturnListOfNotificacoes() throws Exception {
        // Given
        given(notificacaoService.list()).willReturn(List.of(notificacao));

        // When
        mockMvc.perform(get("/notificacoes"))
                // Then
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].tipo").value(notificacao.getTipo()))
                .andExpect(jsonPath("$[0].descricao").value(notificacao.getDescricao()))
                .andExpect(jsonPath("$[0].emailDestinatario").value(notificacao.getEmailDestinatario()))
                .andExpect(jsonPath("$[0].dataEnvio").value(notificacao.getDataEnvio().toString()));
    }

    @Test
    @DisplayName("Should return empty list when no notifications exist")
    public void shouldReturnEmptyListWhenNoNotificationsExist() throws Exception {
        // Given
        given(notificacaoService.list()).willReturn(List.of());

        // When
        mockMvc.perform(get("/notificacoes"))
                // Then
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));
    }

    @Test
    @DisplayName("Should return notification when GET /notificacoes/{id} is called with valid id")
    public void shouldReturnNotificationWhenValidId() throws Exception {
        // Given
        given(notificacaoService.findById(1L)).willReturn(Optional.of(notificacao));

        // When
        mockMvc.perform(get("/notificacoes/{id}", 1L))
                // Then
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.tipo").value(notificacao.getTipo()))
                .andExpect(jsonPath("$.descricao").value(notificacao.getDescricao()))
                .andExpect(jsonPath("$.emailDestinatario").value(notificacao.getEmailDestinatario()))
                .andExpect(jsonPath("$.dataEnvio").value(notificacao.getDataEnvio().toString()));
    }

    @Test
    @DisplayName("Should return 404 when GET /notificacoes/{id} is called with invalid id")
    public void shouldReturn404WhenInvalidId() throws Exception {
        // Given
        given(notificacaoService.findById(1L)).willReturn(Optional.empty());

        // When
        mockMvc.perform(get("/notificacoes/{id}", 1L))
                // Then
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Should create new notification when POST /notificacoes is called")
    public void shouldCreateNewNotification() throws Exception {
        // Given
        given(notificacaoService.saveOrUpdate(any(Notificacao.class))).willReturn(notificacao);

        // When
        mockMvc.perform(post("/notificacoes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(notificacaoCreateDto)))
                // Then
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.tipo").value(notificacao.getTipo()))
                .andExpect(jsonPath("$.descricao").value(notificacao.getDescricao()))
                .andExpect(jsonPath("$.emailDestinatario").value(notificacao.getEmailDestinatario()))
                .andExpect(jsonPath("$.dataEnvio").value(notificacao.getDataEnvio().toString()));
    }

    @Test
    @DisplayName("Should update notification when PUT /notificacoes/{id} is called with valid id")
    public void shouldUpdateNotificationWhenValidId() throws Exception {
        // Given
        given(notificacaoService.existsById(1L)).willReturn(true);
        given(notificacaoService.saveOrUpdate(any(Notificacao.class))).willReturn(notificacao);

        // When
        mockMvc.perform(put("/notificacoes/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(notificacaoUpdateDto)))
                // Then
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.tipo").value(notificacao.getTipo()))
                .andExpect(jsonPath("$.descricao").value(notificacao.getDescricao()))
                .andExpect(jsonPath("$.emailDestinatario").value(notificacao.getEmailDestinatario()))
                .andExpect(jsonPath("$.dataEnvio").value(notificacao.getDataEnvio().toString()));
    }

    @Test
    @DisplayName("Should return 404 when PUT /notificacoes/{id} is called with invalid id")
    public void shouldReturn404WhenUpdatingWithInvalidId() throws Exception {
        // Given
        given(notificacaoService.existsById(1L)).willReturn(false);

        // When
        mockMvc.perform(put("/notificacoes/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(notificacaoUpdateDto)))
                // Then
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Should delete notification when DELETE /notificacoes/{id} is called with valid id")
    public void shouldDeleteNotificationWhenValidId() throws Exception {
        // Given
        given(notificacaoService.existsById(1L)).willReturn(true);
        doNothing().when(notificacaoService).delete(1L);

        // When
        mockMvc.perform(delete("/notificacoes/{id}", 1L))
                // Then
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Should return 404 when DELETE /notificacoes/{id} is called with invalid id")
    public void shouldReturn404WhenDeletingWithInvalidId() throws Exception {
        // Given
        given(notificacaoService.existsById(1L)).willReturn(false);

        // When
        mockMvc.perform(delete("/notificacoes/{id}", 1L))
                // Then
                .andExpect(status().isNotFound());
    }
}
