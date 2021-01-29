package com.matera.bootcamp.digitalbank.entity;

import com.matera.bootcamp.digitalbank.enumerator.SituacaoConta;
import lombok.*;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "db_conta")
public class Conta extends EntidadeBase{

    @Column(precision = 4, nullable = false)
    private Integer numeroAgencia;

    @Column(precision = 12, nullable = false)
    private Long numeroConta;

    @Column(precision = 20, scale = 2, nullable = false )
    private BigDecimal saldo;

    @Enumerated(EnumType.STRING)
    @Column
    private SituacaoConta situacao;

    @OneToOne
    @JoinColumn(name = "id_cliente", nullable = false)
    private Cliente cliente;

    @OneToMany(mappedBy = "conta")
    private List<Lancamento> lancamentos;

}