package com.matera.bootcamp.digitalbank.dto.response;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@Getter
@Setter
@JsonInclude(Include.NON_NULL)
public class ExtratoResponseDto {

    private ContaResponseDto conta;
    private List<ComprovantesResponseDto> lancamentos;

}
