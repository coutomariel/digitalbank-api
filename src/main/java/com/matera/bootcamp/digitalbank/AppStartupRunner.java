package com.matera.bootcamp.digitalbank;


import com.matera.bootcamp.digitalbank.entity.Cliente;
import com.matera.bootcamp.digitalbank.repository.ClienteRepository;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class AppStartupRunner implements ApplicationRunner {


    ClienteRepository clienteRepository;

    public AppStartupRunner(ClienteRepository clienteRepository) {
        this.clienteRepository = clienteRepository;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        System.out.println("Hello world!");

//        Cliente cliente = new Cliente(1L,"João", "99915153181", "5199551814",
//        new BigDecimal(1000), "Logradouro", 99915, "Compl", "Bairro",
//        "Cidade", "UF", "95990515");

        Cliente cliente = Cliente
                .builder().
                        nome("João da Couves").
                        telefone("5144151832").
                        cpf("02151541732").
                        cep("99515781").
                        logradouro("Logradouro").
                        complemento("Apto").
                        bairro("Bairro").
                        cidade("Maringá").build();




        Cliente clienteSalvo = clienteRepository.save(cliente);
        System.out.println("Cliente salvo -> " + clienteSalvo);

        Cliente clientePesquisado = clienteRepository.findByCpf("99915153181").orElse(null);
        System.out.println("Cliente pesquisado -> " + clienteSalvo);


        Cliente clientePesquisadoNaQuery = clienteRepository.buscaPorCpf("99915153181").orElse(null);
        System.out.println("Cliente pesquisado na query -> " + clientePesquisadoNaQuery);

        Cliente clientePesquisadoNativeQuery = clienteRepository.buscaPorCpf("99915153181").orElse(null);
        System.out.println("Cliente pesquisado native query -> " + clientePesquisadoNativeQuery);

    }
}
