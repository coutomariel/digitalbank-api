package com.matera.bootcamp.digitalbank.dto.response;

import lombok.*;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@JsonInclude(Include.NON_NULL)
public class ContaResponseDto {
    private Long idCliente;
    private Long idConta;
    private Integer numeroAgencia;
    private Long numeroConta;
    private String status;
    private BigDecimal saldo;
}
