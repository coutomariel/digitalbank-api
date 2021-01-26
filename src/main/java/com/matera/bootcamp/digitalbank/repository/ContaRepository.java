package com.matera.bootcamp.digitalbank.repository;

import com.matera.bootcamp.digitalbank.entity.Conta;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ContaRepository extends JpaRepository<Conta, Long> {
}
