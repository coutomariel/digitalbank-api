package com.matera.bootcamp.digitalbank.repository;

import com.matera.bootcamp.digitalbank.entity.Transferencia;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransferenciaRepository extends JpaRepository<Transferencia, Long> {
}
