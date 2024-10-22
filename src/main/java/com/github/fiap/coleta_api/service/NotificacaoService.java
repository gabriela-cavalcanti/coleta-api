package com.github.fiap.coleta_api.service;

import java.util.List;
import java.util.Optional;

import com.github.fiap.coleta_api.model.Notificacao;
import org.springframework.stereotype.Service;

import com.github.fiap.coleta_api.repository.NotificacaoRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class NotificacaoService {
    private final NotificacaoRepository notificacaoRepository;

    public List<Notificacao> list() {
        return notificacaoRepository.findAll();
    }

    public Notificacao saveOrUpdate(Notificacao notificacao) {
        return notificacaoRepository.save(notificacao);
    }
    
    public void delete(long id) {
        notificacaoRepository.deleteById(id);
    }

    public Optional<Notificacao> findById(long id) {
        return notificacaoRepository.findById(id);
    }
    
    public boolean existsById(long id) {
        return notificacaoRepository.existsById(id);
    }
}