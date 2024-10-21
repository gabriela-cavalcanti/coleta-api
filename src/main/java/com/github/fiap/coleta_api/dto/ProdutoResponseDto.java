package com.github.fiap.coleta_api.dto;

import org.modelmapper.ModelMapper;

import com.github.fiap.coleta_api.model.Produto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProdutoResponseDto {
    private Long id;
    private String nome;  
    private static final ModelMapper mapper = new ModelMapper();
    
    public ProdutoResponseDto toDto(Produto produto) {
        return  mapper.map(produto, ProdutoResponseDto.class);
    }
}
