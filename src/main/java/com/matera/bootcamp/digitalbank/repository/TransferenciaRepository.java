package com.matera.bootcamp.digitalbank.repository;

import com.matera.bootcamp.digitalbank.entity.Transferencia;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface TransferenciaRepository extends JpaRepository<Transferencia, Long> {

    @Query("SELECT t " +
            "FROM   Transferencia t " +
            "WHERE  t.lancamentoDebito.id  = :idLancamento OR " +
            "       t.lancamentoCredito.id = :idLancamento ")
    Optional<Transferencia> consultaTransferenciaPorIdLancamento(@Param("idLancamento") Long idLancamento);
}
