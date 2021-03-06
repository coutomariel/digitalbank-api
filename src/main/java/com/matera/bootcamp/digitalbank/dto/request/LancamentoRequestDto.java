package com.matera.bootcamp.digitalbank.dto.request;

import java.math.BigDecimal;

import javax.validation.constraints.Digits;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LancamentoRequestDto {

    @NotNull
    @Digits(integer = 18, fraction = 2)
    private BigDecimal valor;

    @NotBlank
    @Size(max = 50)
    private String descricao;

}