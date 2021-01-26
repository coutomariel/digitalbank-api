package com.matera.bootcamp.digitalbank.entity;

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
public class Conta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(precision = 4, nullable = false)
    private Integer numeroAgencia;

    @Column(precision = 12, nullable = false)
    private Long numeroConta;

    @Column(precision = 20, scale = 2, nullable = false )
    private BigDecimal saldo;

    @Column(length = 1, nullable = false)
    private String status;

    @OneToOne
    @JoinColumn(name = "id_cliente", nullable = false)
    private Cliente cliente;

    @OneToMany(mappedBy = "conta")
    private List<Lancamento> lancamentos;

}