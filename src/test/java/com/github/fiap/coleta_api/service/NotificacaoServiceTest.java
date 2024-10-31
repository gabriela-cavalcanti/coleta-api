package com.github.fiap.coleta_api.service;

import com.github.fiap.coleta_api.model.Notificacao;
import com.github.fiap.coleta_api.repository.NotificacaoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class NotificacaoServiceTest {

    @Mock
    private NotificacaoRepository notificacaoRepository;

    @InjectMocks
    private NotificacaoService notificacaoService;

    private Notificacao notificacao;

    @BeforeEach
    public void setup() {
        notificacao = new Notificacao();
        notificacao.setId(1L);
        notificacao.setTipo("TIPO_TESTE");
        notificacao.setDescricao("DESCRICAO_TESTE");
        notificacao.setEmailDestinatario("EMAIL_TESTE");
        notificacao.setDataEnvio(LocalDate.of(2024, 10, 21));
    }

    @Test
    @DisplayName("Should return list of notifications")
    public void testList() {
        // Given
        List<Notificacao> notificacoes = Collections.singletonList(notificacao);
        when(notificacaoRepository.findAll()).thenReturn(notificacoes);

        // When
        List<Notificacao> result = notificacaoService.list();

        // Then
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(notificacao, result.get(0));
        verify(notificacaoRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Should return empty list when no notifications exist")
    public void testListWhenEmpty() {
        // Given
        when(notificacaoRepository.findAll()).thenReturn(List.of());

        // When
        List<Notificacao> result = notificacaoService.list();

        // Then
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(notificacaoRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Should save or update a notification")
    public void testSaveOrUpdate() {
        // Given
        when(notificacaoRepository.save(notificacao)).thenReturn(notificacao);

        // When
        Notificacao result = notificacaoService.saveOrUpdate(notificacao);

        // Then
        assertNotNull(result);
        assertEquals(notificacao, result);
        verify(notificacaoRepository, times(1)).save(notificacao);
    }

    @Test
    @DisplayName("Should delete a notification by ID")
    public void testDelete() {
        // Given
        doNothing().when(notificacaoRepository).deleteById(1L);

        // When
        notificacaoService.delete(1L);

        // Then
        verify(notificacaoRepository, times(1)).deleteById(1L);
    }

    @Test
    @DisplayName("Should find a notification by ID")
    public void testFindById() {
        // Given
        when(notificacaoRepository.findById(1L)).thenReturn(Optional.of(notificacao));

        // When
        Optional<Notificacao> result = notificacaoService.findById(1L);

        // Then
        assertTrue(result.isPresent());
        assertEquals(notificacao, result.get());
        verify(notificacaoRepository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("Should return empty when notification not found by ID")
    public void testFindByIdNotFound() {
        // Given
        when(notificacaoRepository.findById(1L)).thenReturn(Optional.empty());

        // When
        Optional<Notificacao> result = notificacaoService.findById(1L);

        // Then
        assertFalse(result.isPresent());
        verify(notificacaoRepository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("Should check if notification exists by ID - exists")
    public void testExistsById() {
        // Given
        when(notificacaoRepository.existsById(1L)).thenReturn(true);

        // When
        boolean exists = notificacaoService.existsById(1L);

        // Then
        assertTrue(exists);
        verify(notificacaoRepository, times(1)).existsById(1L);
    }

    @Test
    @DisplayName("Should check if notification exists by ID - does not exist")
    public void testExistsByIdFalse() {
        // Given
        when(notificacaoRepository.existsById(1L)).thenReturn(false);

        // When
        boolean exists = notificacaoService.existsById(1L);

        // Then
        assertFalse(exists);
        verify(notificacaoRepository, times(1)).existsById(1L);
    }
}
