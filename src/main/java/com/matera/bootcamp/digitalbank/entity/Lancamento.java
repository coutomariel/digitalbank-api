package com.matera.bootcamp.digitalbank.entity;

import lombok.*;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "db_lancamentos")
public class Lancamento extends EntidadeBase{

    @Column(length = 50, nullable = false)
    private String codigoAutenticacao;

    @Column(nullable = false)
    private LocalDate dataHora;

    @Column(precision = 2, scale = 2, nullable = false)
    private BigDecimal valor;

    @Column(length = 1, nullable = false)
    private String natureza;

    @Column(length = 1, nullable = false)
    private String tipoLancamento;

    @Column(length = 50, nullable = false)
    private String descricao;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "id_conta", nullable = false)
    private Conta conta;

}
