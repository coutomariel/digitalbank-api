package com.matera.bootcamp.digitalbank.dto.request;


import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ClienteRequestDto {

    private String nome;
    private String cpf;
    private Long telefone;
    private BigDecimal rendaMensal;
    private String logradouro;
    private Integer numero;
    private String complemento;
    private String bairro;
    private String cidade;
    private String uf;
    private String cep;

}
