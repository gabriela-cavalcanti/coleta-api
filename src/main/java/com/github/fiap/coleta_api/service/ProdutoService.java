package com.github.fiap.coleta_api.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.github.fiap.coleta_api.model.Produto;
import com.github.fiap.coleta_api.repository.ProdutoRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProdutoService {
    private final ProdutoRepository produtoRepository;

    public List<Produto> list() {
        return produtoRepository.findAll();
    }

    public Produto saveOrUpdate(Produto produto) {
        return produtoRepository.save(produto);
    }
    
    public void delete(long id) {
        produtoRepository.deleteById(id);
    }

    public Optional<Produto> findById(long id) {
        return produtoRepository.findById(id);
    }
    
    public boolean existsById(long id) {
        return produtoRepository.existsById(id);
    }
}