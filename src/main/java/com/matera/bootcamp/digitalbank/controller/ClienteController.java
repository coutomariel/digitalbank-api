package com.matera.bootcamp.digitalbank.controller;

import java.util.List;

import com.matera.bootcamp.digitalbank.dto.request.ClienteRequestDto;
import com.matera.bootcamp.digitalbank.dto.response.ClienteResponseDto;
import com.matera.bootcamp.digitalbank.dto.response.ContaResponseDto;
import com.matera.bootcamp.digitalbank.dto.response.ResponseDto;
import com.matera.bootcamp.digitalbank.service.ClienteService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/clientes")
public class ClienteController extends controllerBase {

    final ClienteService clienteService;

    public ClienteController(ClienteService clienteService) {
        this.clienteService = clienteService;
    }

    @PostMapping
    public ResponseEntity<ResponseDto<ContaResponseDto>> cadastra(@RequestBody ClienteRequestDto clienteRequestDto) {
        ContaResponseDto contaResponseDto = clienteService.cadastrar(clienteRequestDto);
        return ResponseEntity.status(HttpStatus.OK).body(new ResponseDto<>(contaResponseDto));
    }

    @GetMapping
    public ResponseEntity<ResponseDto<List<ClienteResponseDto>>> consultaTodos() {
        List<ClienteResponseDto> clientes = clienteService.consultaTodos();
        return ResponseEntity.status(HttpStatus.OK).body(new ResponseDto<>(clientes));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseDto<ClienteResponseDto>> consultaPorId(@PathVariable Long id) {
        ClienteResponseDto cliente = clienteService.consultar(id);
        return ResponseEntity.status(HttpStatus.OK).body(new ResponseDto<>(cliente));
    }

    @GetMapping("/{id}/conta")
    public ResponseEntity<ResponseDto<ContaResponseDto>> consultaContaPorIdCliente(@PathVariable Long id) {
        ContaResponseDto conta = clienteService.consultaContaPorIdCliente(id);
        return ResponseEntity.status(HttpStatus.OK).body(new ResponseDto<>(conta));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ResponseDto<Void>> atualizar(@PathVariable Long id, @RequestBody ClienteRequestDto dto) {
        clienteService.atualizar(id, dto);
        return ResponseEntity.noContent().build();
    }

}
