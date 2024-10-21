package com.github.fiap.coleta_api.dto;

import org.modelmapper.ModelMapper;

import com.github.fiap.coleta_api.model.Produto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProdutoCreateDto {    
    private String nome;  
    private static final ModelMapper mapper = new ModelMapper();
    
    public Produto toModel() {
        return  mapper.map(this, Produto.class);
    }
}
