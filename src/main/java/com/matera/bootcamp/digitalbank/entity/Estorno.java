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
@Table(name = "db_estorno")
public class Estorno {
    @Id@GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "id_lancamento_original", nullable = false)
    private Lancamento lancamentoOriginal;

    @OneToOne
    @JoinColumn(name = "id_lancamento_estorno", nullable = false)
    private Lancamento lancamentoEstorno;

}
