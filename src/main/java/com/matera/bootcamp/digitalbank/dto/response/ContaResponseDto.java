package com.matera.bootcamp.digitalbank.dto.response;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class ContaResponseDto {
    private Long idCliente;
    private Long idConta;
    private Integer numeroAgencia;
    private Long numeroConta;
    private String status;
    private BigDecimal saldo;
}
