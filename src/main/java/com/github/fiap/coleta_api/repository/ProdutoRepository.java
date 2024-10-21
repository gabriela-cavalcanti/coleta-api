package com.github.fiap.coleta_api.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.github.fiap.coleta_api.model.Produto;

public interface ProdutoRepository extends JpaRepository<Produto, Long> {
    
}
