package com.matera.bootcamp.digitalbank.controller;

import java.time.LocalDate;
import java.util.List;

import com.matera.bootcamp.digitalbank.dto.request.LancamentoRequestDto;
import com.matera.bootcamp.digitalbank.dto.request.TransferenciaRequestDto;
import com.matera.bootcamp.digitalbank.dto.response.ComprovantesResponseDto;
import com.matera.bootcamp.digitalbank.dto.response.ContaResponseDto;
import com.matera.bootcamp.digitalbank.dto.response.ExtratoResponseDto;
import com.matera.bootcamp.digitalbank.dto.response.ResponseDto;
import com.matera.bootcamp.digitalbank.enumerator.TipoLancamento;
import com.matera.bootcamp.digitalbank.service.ContaService;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/contas")
public class ContaController extends controllerBase {

    private final ContaService contaService;

    public ContaController(ContaService contaService) {
        this.contaService = contaService;
    }

    @GetMapping
    public ResponseEntity<ResponseDto<List<ContaResponseDto>>> consultaTodas() {
        List<ContaResponseDto> contas = contaService.consultaTodas();
        return ResponseEntity.ok(new ResponseDto<>(contas));
    }

    @PostMapping("/{id}/depositar")
    public ResponseEntity<ResponseDto<ComprovantesResponseDto>> efetuaDeposito(@PathVariable Long id,
            @RequestBody LancamentoRequestDto lancamentoRequestDTO) {
        ComprovantesResponseDto comprovante = contaService.efetuaLancamento(id, lancamentoRequestDTO,
                TipoLancamento.DEPOSITO);
        return ResponseEntity.ok(new ResponseDto<>(comprovante));
    }

    @PostMapping("/{id}/sacar")
    public ResponseEntity<ResponseDto<ComprovantesResponseDto>> efetuaSaque(@PathVariable Long id,
            @RequestBody LancamentoRequestDto lancamentoRequestDTO) {
        ComprovantesResponseDto comprovante = contaService.efetuaLancamento(id, lancamentoRequestDTO,
                TipoLancamento.SAQUE);
        return ResponseEntity.ok(new ResponseDto<>(comprovante));
    }

    @PostMapping("/{id}/pagar")
    public ResponseEntity<ResponseDto<ComprovantesResponseDto>> efetuaPagamento(@PathVariable Long id,
            @RequestBody LancamentoRequestDto lancamentoRequestDTO) {
        ComprovantesResponseDto comprovante = contaService.efetuaLancamento(id, lancamentoRequestDTO,
                TipoLancamento.PAGAMENTO);
        return ResponseEntity.ok(new ResponseDto<>(comprovante));
    }

    @PostMapping("/{id}/transferencia")
    public ResponseEntity<ResponseDto<ComprovantesResponseDto>> efetuaTransferencia(@PathVariable Long id,
            @RequestBody TransferenciaRequestDto transferenciaRequestDTO) {
        ComprovantesResponseDto comprovante = contaService.efetuaTransferencia(id, transferenciaRequestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(new ResponseDto<>(comprovante));
    }

    @GetMapping(value = "/{id}/lancamentos", params = { "!dataInicial", "!dataFinal" })
    public ResponseEntity<ResponseDto<ExtratoResponseDto>> consultaExtratoCompleto(@PathVariable Long id) {
        ExtratoResponseDto extrato = contaService.consultaExtratoCompleto(id);
        return ResponseEntity.ok(new ResponseDto<>(extrato));
    }

    @GetMapping(value = "/{id}/lancamentos", params = { "dataInicial", "dataFinal" })
    public ResponseEntity<ResponseDto<ExtratoResponseDto>> consultaExtratoPorPeriodo(@PathVariable("id") Long id,
            @RequestParam(value = "dataInicial", required = true) @DateTimeFormat(pattern = "dd-MM-yyyy") LocalDate dataInicial,
            @RequestParam(value = "dataFinal", required = true) @DateTimeFormat(pattern = "dd-MM-yyyy") LocalDate dataFinal) {

        ExtratoResponseDto extratoResponseDTO = contaService.consultaExtratoPorPeriodo(id, dataInicial, dataFinal);

        return ResponseEntity.ok(new ResponseDto<>(extratoResponseDTO));
    }

    @GetMapping("/{id}/lancamentos/{idLancamento}")
    public ResponseEntity<ResponseDto<ComprovantesResponseDto>> consultaComprovanteLancamento(@PathVariable Long id,
            @PathVariable Long idLancamento) {
        ComprovantesResponseDto comprovante = contaService.consultaComprovanteLancamento(id, idLancamento);
        return ResponseEntity.ok(new ResponseDto<>(comprovante));
    }

    @PostMapping("/{id}/lancamentos/{idLancamento}/estornar")
    public ResponseEntity<ResponseDto<ComprovantesResponseDto>> estornaLancamento(@PathVariable Long id,
            @PathVariable Long idLancamento) {
        ComprovantesResponseDto comprovante = contaService.estornaLancamento(id, idLancamento);
        return ResponseEntity.ok(new ResponseDto<>(comprovante));
    }

    @DeleteMapping("/{id}/lancamentos/{idLancamento}")
    public ResponseEntity<ResponseDto<Void>> removeLancamentoEstorno(@PathVariable Long id,
            @PathVariable Long idLancamento) {
        contaService.removeLancamentoEstorno(id, idLancamento);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/bloqueia")
    public ResponseEntity<ResponseDto<Void>> bloqueiaConta(@PathVariable Long id) {
        contaService.bloqueiaConta(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/desbloqueia")
    public ResponseEntity<ResponseDto<Void>> desbloqueiaConta(@PathVariable Long id) {
        contaService.desbloqueiaConta(id);
        return ResponseEntity.noContent().build();
    }

}