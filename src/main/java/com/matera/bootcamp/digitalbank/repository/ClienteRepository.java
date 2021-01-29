package com.matera.bootcamp.digitalbank.repository;

import com.matera.bootcamp.digitalbank.entity.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ClienteRepository extends JpaRepository<Cliente, Long> {

    Optional<Cliente> findByTelefone(Long telefone);
    Optional<Cliente> findByCpf(String cpf);

    @Query("SELECT cli " +
            "FROM Cliente cli " +
            "WHERE cli.cpf = :cpf")
    Optional<Cliente> buscaPorCpf(@Param("cpf") String cpf);

    @Query(value = "SELECT * " +
            "FROM db_cliente cli " +
            "WHERE cli.cpf = :cpf", nativeQuery = true)
    Optional<Cliente> buscaPorCpfNativaQuery(@Param("cpf") String cpf);


}
