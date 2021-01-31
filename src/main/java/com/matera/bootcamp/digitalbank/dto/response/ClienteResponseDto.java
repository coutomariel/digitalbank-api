package com.matera.bootcamp.digitalbank.dto.response;

import lombok.*;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@Getter
@Setter
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(Include.NON_NULL)
public class ClienteResponseDto {

    private Long id;
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
