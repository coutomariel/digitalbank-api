package com.matera.bootcamp.digitalbank.service;

import com.matera.bootcamp.digitalbank.dto.response.ContaResponseDto;
import com.matera.bootcamp.digitalbank.entity.Cliente;
import com.matera.bootcamp.digitalbank.entity.Conta;
import com.matera.bootcamp.digitalbank.repository.ContaRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

@Service
public class ContaService {

    final ContaRepository contaRepository;

    @Value("${agencia.numeroMaximo:3}")
    private Integer numeroMaximoAgencia;

    public ContaService(ContaRepository contaRepository) {
        this.contaRepository = contaRepository;
    }

    @Transactional
    public ContaResponseDto cadastrar(Cliente cliente){
        validaConta(cliente);
        Conta conta = Conta
                .builder()
                    .numeroAgencia(new Random().nextInt(numeroMaximoAgencia)+1)
                    .numeroConta(cliente.getTelefone())
                    .cliente(cliente)
                    .status("A")
                    .saldo(BigDecimal.ZERO)
                .build();

        Conta ContaSalva  = contaRepository.save(conta);
        return EntitytoContaResponseDto(ContaSalva);
    }

    public List<ContaResponseDto> consultaTodas(){
        List<Conta> contas = contaRepository.findAll();
        return contas.stream().map(conta -> EntitytoContaResponseDto(conta)).collect(Collectors.toList());
    }

    public ContaResponseDto consultaContaPorIdCliente(Long id){
        Conta conta = contaRepository.findByClienteId(id)
                .orElseThrow(() -> new RuntimeException(("Conta não encontrada!")));
        return EntitytoContaResponseDto(conta);
    }


    public void bloqueiaConta(Long id){
        Conta conta = consultaPorId(id);
        validaBloqueio(conta);
        conta.setStatus("B");
        contaRepository.save(conta);
    }

    private void validaBloqueio(Conta conta) {
        if("B".equals(conta.getStatus())){
            throw new RuntimeException("Conta de ID "+ conta.getId() + "já se encontra bloqueada.");
        }

    }

    public void desbloqueiaConta(Long id){
        Conta conta = consultaPorId(id);
        validaDesbloqueio(conta);
        conta.setStatus("A");
        contaRepository.save(conta);

    }

    private void validaDesbloqueio(Conta conta) {
        if("A".equals(conta.getStatus())){
            throw  new RuntimeException("Conta de ID " + conta.getId() + "não está bloqueada.");
        }
    }


    public Conta consultaPorId(Long id){
        return contaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Conta  de id "+ id +"não encontrada"));
    }

    private void validaConta(Cliente cliente) {
        if(contaRepository.findByNumeroConta(cliente.getTelefone()).isPresent()){
            throw new RuntimeException("Já existe uma conta com o número de telefone informado!");
        }
    }

    private ContaResponseDto EntitytoContaResponseDto(Conta conta) {
        ContaResponseDto contaResponseDto = ContaResponseDto
                .builder()
                    .idCliente(conta.getCliente().getId())
                    .idConta(conta.getId())
                    .numeroAgencia(conta.getNumeroAgencia())
                    .numeroConta(conta.getNumeroConta())
                    .status(conta.getStatus())
                    .saldo(conta.getSaldo())
                .build();
        return contaResponseDto;
    }

}
