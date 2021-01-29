package com.matera.bootcamp.digitalbank.service;

import com.matera.bootcamp.digitalbank.dto.request.ClienteRequestDto;
import com.matera.bootcamp.digitalbank.dto.response.ClienteResponseDto;
import com.matera.bootcamp.digitalbank.dto.response.ContaResponseDto;
import com.matera.bootcamp.digitalbank.entity.Cliente;
import com.matera.bootcamp.digitalbank.exception.ServiceException;
import com.matera.bootcamp.digitalbank.repository.ClienteRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ClienteService {

    final ClienteRepository clienteRepository;
    final ContaService contaService;

    public ClienteService(ClienteRepository clienteRepository, ContaService contaService) {
        this.clienteRepository = clienteRepository;
        this.contaService = contaService;
    }

    @Transactional
    public ContaResponseDto cadastrar(ClienteRequestDto clienteRequestDto){
        validaCadadastro(clienteRequestDto.getCpf());
        Cliente clienteToSave = dtoToEntity(clienteRequestDto, new Cliente());
        clienteRepository.save(clienteToSave);
        return contaService.cadastrar(clienteToSave);
    }

    @Transactional
    public void atualizar(Long id, ClienteRequestDto dto){
        validaAtualizacao(id, dto);
        Cliente clienteAtualizado = dtoToEntity(dto, consultar(id));
        clienteRepository.save(clienteAtualizado);
    }

    public List<ClienteResponseDto> consultaTodos(){
        List<Cliente> clientes = clienteRepository.findAll();
        return clientes.stream()
                .map(cliente -> entityToDto(cliente)).collect(Collectors.toList());
    }

    public Cliente consultar(Long id){
        return clienteRepository.findById(id)
                .orElseThrow(() -> new ServiceException("Cliente de id "+ id + " não encontrado!"));
    }

    private void validaCadadastro(String cpf) {
        if(clienteRepository.findByCpf(cpf).isPresent()){
            throw new ServiceException("Já existe cliente com este cpf!");
        }
    }

    private void validaAtualizacao(Long id, ClienteRequestDto clienteRequestDto) {
        Optional<Cliente> cliente = clienteRepository.findByCpf(clienteRequestDto.getCpf());

        if (cliente.isPresent() && !cliente.get().getId().equals(id)){
            throw new ServiceException("Cpf da conta não pode ser alterado!");
        }
    }

    private Cliente dtoToEntity(ClienteRequestDto clienteRequestDto, Cliente cliente) {
        cliente.setBairro(clienteRequestDto.getBairro());
        cliente.setCep(clienteRequestDto.getCep());
        cliente.setCidade(clienteRequestDto.getCidade());
        cliente.setComplemento(clienteRequestDto.getComplemento());
        cliente.setCpf(clienteRequestDto.getCpf());
        cliente.setUf(clienteRequestDto.getUf());
        cliente.setLogradouro(clienteRequestDto.getLogradouro());
        cliente.setNome(clienteRequestDto.getNome());
        cliente.setNumero(clienteRequestDto.getNumero());
        cliente.setRendaMensal(clienteRequestDto.getRendaMensal());
        cliente.setTelefone(clienteRequestDto.getTelefone());
        return cliente;
    }

    private ClienteResponseDto entityToDto(Cliente cliente){
        ClienteResponseDto dto = ClienteResponseDto
                .builder()
                    .id(cliente.getId())
                    .bairro(cliente.getBairro())
                    .cep(cliente.getCep())
                    .cidade(cliente.getCidade())
                    .complemento(cliente.getComplemento())
                    .cpf(cliente.getCpf())
                    .uf(cliente.getUf())
                    .logradouro(cliente.getLogradouro())
                    .nome(cliente.getNome())
                    .numero(cliente.getNumero())
                    .rendaMensal(cliente.getRendaMensal())
                    .telefone(cliente.getTelefone())
                .build();
        return dto;
    }
}
