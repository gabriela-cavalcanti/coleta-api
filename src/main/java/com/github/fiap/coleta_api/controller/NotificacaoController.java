package com.github.fiap.coleta_api.controller;

import com.github.fiap.coleta_api.dto.NotificacaoCreateDto;
import com.github.fiap.coleta_api.dto.NotificacaoResponseDto;
import com.github.fiap.coleta_api.dto.NotificacaoUpdateDto;
import com.github.fiap.coleta_api.model.Notificacao;
import com.github.fiap.coleta_api.service.NotificacaoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/notificacoes")
@RequiredArgsConstructor
public class NotificacaoController {
    private final NotificacaoService notificacaoService;

    @GetMapping
    public ResponseEntity<List<NotificacaoResponseDto>> list() {

        List<NotificacaoResponseDto> responseDtos = notificacaoService.list()
                .stream()
                .map(notificacao -> new NotificacaoResponseDto().toDto(notificacao))
                .toList();
        return ResponseEntity.ok().body(responseDtos);
    }

    @GetMapping("{id}")
    public ResponseEntity<NotificacaoResponseDto> findById(@PathVariable Long id) {

        NotificacaoResponseDto responseDto = notificacaoService
                .findById(id)
                .map(notificacao -> new NotificacaoResponseDto().toDto(notificacao))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Notificação não encontrada"));

        return ResponseEntity.ok().body(responseDto);
    }

    @PostMapping
    public ResponseEntity<NotificacaoResponseDto> create(@RequestBody NotificacaoCreateDto requestDto) {
        Notificacao notificacao = notificacaoService.saveOrUpdate(requestDto.toModel());

        NotificacaoResponseDto responseDto = new NotificacaoResponseDto().toDto(notificacao);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                .buildAndExpand(notificacao.getId()).toUri();
        return ResponseEntity.created(location).body(responseDto);
    }

    @PutMapping("{id}")
    public ResponseEntity<NotificacaoResponseDto> update(@PathVariable Long id, @RequestBody NotificacaoUpdateDto requestDto) {
        if (!notificacaoService.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Notificação não encontrada");
        }

        Notificacao notificacao = notificacaoService.saveOrUpdate(requestDto.toModel());

        NotificacaoResponseDto responseDto = new NotificacaoResponseDto().toDto(notificacao);
        return ResponseEntity.ok().body(responseDto);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<?> update(@PathVariable Long id) {
        if (!notificacaoService.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Notificação não encontrada");
        }

        notificacaoService.delete(id);
        return ResponseEntity.ok().build();
    }

}
