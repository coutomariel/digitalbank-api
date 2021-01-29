package com.matera.bootcamp.digitalbank;


import com.matera.bootcamp.digitalbank.dto.request.ClienteRequestDto;
import com.matera.bootcamp.digitalbank.dto.response.ContaResponseDto;
import com.matera.bootcamp.digitalbank.entity.Cliente;
import com.matera.bootcamp.digitalbank.entity.Conta;
import com.matera.bootcamp.digitalbank.repository.ClienteRepository;
import com.matera.bootcamp.digitalbank.service.ClienteService;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class AppStartupRunner implements ApplicationRunner {


    ClienteRepository clienteRepository;
    ClienteService clienteService;

    public AppStartupRunner(ClienteRepository clienteRepository, ClienteService clienteService) {
        this.clienteRepository = clienteRepository;
        this.clienteService = clienteService;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {

        ClienteRequestDto cliente = ClienteRequestDto
                .builder().
                        nome("João da Neves").
                        telefone(5144151832L).
                        cpf("02151541732").
                        rendaMensal(new BigDecimal(1000)).
                        cep("99515781").
                        logradouro("Logradouro").
                        numero(295).
                        complemento("Apto").
                        bairro("Bairro").
                        cidade("Maringá").
                        uf("PR")
                .build();

        ClienteRequestDto segundoCliente = ClienteRequestDto
                .builder().
                        nome("Brandon Stark").
                        telefone(5144153321L).
                        cpf("32541483098").
                        rendaMensal(new BigDecimal(1000)).
                        cep("99515781").
                        logradouro("Logradouro").
                        numero(295).
                        complemento("Apto").
                        bairro("Bairro").
                        cidade("Maringá").
                        uf("PR")
                .build();


        //Usando serviço de cadastro
        ContaResponseDto conta  = clienteService.cadastrar(cliente);
        ContaResponseDto outraConta  = clienteService.cadastrar(segundoCliente);

        //Usando consulta pelo id do serviço de cliente
        System.out.println("Cliente salvo -> " + clienteService.consultar(conta.getIdCliente()));

        //Criando segundo cliente

        //Consultando todos
        System.out.println("Lista de clientes" + clienteService.consultaTodos());

        ClienteRequestDto segundoClienteAlterado = ClienteRequestDto
                .builder().
                        nome("Brandon Stark").
                        telefone(5144153321L).
                        cpf("32541483098").
                        rendaMensal(new BigDecimal(1000)).
                        cep("99515781").
                        logradouro("Logradouro").
                        numero(295).
                        complemento("Apto").
                        bairro("Outro Bairro").
                        cidade("Maringá").
                        uf("PR")
                .build();

        Cliente clienteAlterado = clienteService.consultar(2L);
        clienteService.atualizar(2L, segundoClienteAlterado);
        System.out.println("Cliente após alteração -> "+ clienteService.consultar(2L));
        System.out.println("Qtde de clientes: " + clienteService.consultaTodos().size());
    }
}
