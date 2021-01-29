package com.matera.bootcamp.digitalbank.dto.response;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ExtratoResponseDto {

    private ContaResponseDto conta;
    private List<ComprovantesResponseDto> lancamentos;

}
