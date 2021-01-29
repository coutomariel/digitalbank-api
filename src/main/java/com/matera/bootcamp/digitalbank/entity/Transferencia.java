package com.matera.bootcamp.digitalbank.entity;

import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "db_transferencia")
public class Transferencia extends EntidadeBase{

    @OneToOne
    @JoinColumn(name = "id_lancamento_debito", nullable = false)
    private Lancamento lancamentoDebito;

    @OneToOne
    @JoinColumn(name = "id_lancamento_credito", nullable = false)
    private Lancamento lancamentoCredito;

}
