package com.matera.bootcamp.digitalbank.repository;

import com.matera.bootcamp.digitalbank.entity.Estorno;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EstornoRepository extends JpaRepository<Estorno, Long > {
}
