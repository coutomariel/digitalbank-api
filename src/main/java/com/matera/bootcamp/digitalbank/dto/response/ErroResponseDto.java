package com.matera.bootcamp.digitalbank.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@JsonInclude(Include.NON_NULL)
public class ErroResponseDto {

    private String campo;
    private String mensagem;

    public ErroResponseDto(String mensagem) {
        this.mensagem = mensagem;
    }

}
