package com.matera.bootcamp.digitalbank.repository;

import com.matera.bootcamp.digitalbank.entity.Lancamento;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LancamentoRepository extends JpaRepository<Lancamento, Long> {
}
