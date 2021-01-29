package com.matera.bootcamp.digitalbank.repository;

import com.matera.bootcamp.digitalbank.entity.Estorno;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EstornoRepository extends JpaRepository<Estorno, Long > {
    Optional<Estorno> findByLancamentoOriginal_Id(Long lancamentoOriginalId);
}
