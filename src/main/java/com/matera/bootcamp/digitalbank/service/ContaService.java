package com.matera.bootcamp.digitalbank.service;

import com.matera.bootcamp.digitalbank.dto.request.LancamentoRequestDto;
import com.matera.bootcamp.digitalbank.dto.request.TransferenciaRequestDto;
import com.matera.bootcamp.digitalbank.dto.response.ComprovantesResponseDto;
import com.matera.bootcamp.digitalbank.dto.response.ContaResponseDto;
import com.matera.bootcamp.digitalbank.dto.response.ExtratoResponseDto;
import com.matera.bootcamp.digitalbank.entity.Cliente;
import com.matera.bootcamp.digitalbank.entity.Conta;
import com.matera.bootcamp.digitalbank.entity.Lancamento;
import com.matera.bootcamp.digitalbank.enumerator.Natureza;
import com.matera.bootcamp.digitalbank.enumerator.SituacaoConta;
import com.matera.bootcamp.digitalbank.enumerator.TipoLancamento;
import com.matera.bootcamp.digitalbank.exception.ServiceException;
import com.matera.bootcamp.digitalbank.repository.ContaRepository;
import com.matera.bootcamp.digitalbank.utils.DigitalBankUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

@Service
public class ContaService {

    final ContaRepository contaRepository;
    final LancamentoService lancamentoService;

    @Value("${agencia.numeroMaximo:3}")
    private Integer numeroMaximoAgencia;

    public ContaService(ContaRepository contaRepository, LancamentoService lancamentoService) {
        this.contaRepository = contaRepository;
        this.lancamentoService = lancamentoService;
    }

    @Transactional
    public ContaResponseDto cadastrar(Cliente cliente){
        validaConta(cliente);
        Conta conta = Conta
                .builder()
                    .numeroAgencia(new Random().nextInt(numeroMaximoAgencia)+1)
                    .numeroConta(cliente.getTelefone())
                    .cliente(cliente)
                    .situacao(SituacaoConta.ABERTA)
                    .saldo(BigDecimal.ZERO)
                .build();

        Conta ContaSalva  = contaRepository.save(conta);
        return entitytoContaResponseDto(ContaSalva);
    }

    @Transactional
    public ComprovantesResponseDto efetuaLancamento(Long id, LancamentoRequestDto lancamentoRequestDTO, TipoLancamento tipoLancamento) {
        Conta conta = consultaPorId(id);

        Lancamento lancamento = insereLancamento(lancamentoRequestDTO, conta, defineNaturezaPorTipoLancamento(tipoLancamento), tipoLancamento);

        return lancamentoService.entidadeParaComprovanteResponseDTO(lancamento);
    }

    @Transactional
    public ComprovantesResponseDto efetuaTransferencia(Long id, TransferenciaRequestDto transferenciaRequestDTO) {
        Conta contaDebito = consultaPorId(id);

        Conta contaCredito = contaRepository.findByNumeroAgenciaAndNumeroConta(transferenciaRequestDTO.getNumeroAgencia(), transferenciaRequestDTO.getNumeroConta())
                .orElseThrow(() -> new ServiceException("Conta de agência " + transferenciaRequestDTO.getNumeroAgencia() + " e número " + transferenciaRequestDTO.getNumeroConta().toString() + " não encontrada."));

        Lancamento lancamentoDebito = insereLancamento(new LancamentoRequestDto(transferenciaRequestDTO.getValor(), transferenciaRequestDTO.getDescricao()), contaDebito, Natureza.DEBITO, TipoLancamento.TRANSFERENCIA);
        Lancamento lancamentoCredito = insereLancamento(new LancamentoRequestDto(transferenciaRequestDTO.getValor(), transferenciaRequestDTO.getDescricao()), contaCredito, Natureza.CREDITO, TipoLancamento.TRANSFERENCIA);

        return lancamentoService.efetuaTransferencia(lancamentoDebito, lancamentoCredito);
    }

    public ExtratoResponseDto consultaExtratoCompleto(Long id) {
        Conta conta = consultaPorId(id);

        List<ComprovantesResponseDto> comprovantesResponseDTO = lancamentoService.consultaExtratoCompleto(conta);

        ExtratoResponseDto extratoResponseDTO = new ExtratoResponseDto();
        extratoResponseDTO.setConta(entitytoContaResponseDto(conta));
        extratoResponseDTO.setLancamentos(comprovantesResponseDTO);

        return extratoResponseDTO;
    }

    public ExtratoResponseDto consultaExtratoPorPeriodo(Long id, LocalDate dataInicial, LocalDate dataFinal) {
        Conta conta = consultaPorId(id);

        List<ComprovantesResponseDto> comprovantesResponseDTO = lancamentoService.consultaExtratoPorPeriodo(conta, dataInicial, dataFinal);

        ExtratoResponseDto extratoResponseDTO = new ExtratoResponseDto();
        extratoResponseDTO.setConta(entitytoContaResponseDto(conta));
        extratoResponseDTO.setLancamentos(comprovantesResponseDTO);

        return extratoResponseDTO;
    }

    @Transactional
    public ComprovantesResponseDto estornaLancamento(Long idConta, Long idLancamento) {
        return lancamentoService.estornaLancamento(idConta, idLancamento);
    }

    public ComprovantesResponseDto consultaComprovanteLancamento(Long idConta, Long idLancamento) {
        return lancamentoService.consultaComprovanteLancamento(idConta, idLancamento);
    }

    @Transactional
    public void removeLancamentoEstorno(Long idConta, Long idLancamento) {
        lancamentoService.removeLancamentoEstorno(idConta, idLancamento);
    }

    private Lancamento insereLancamento(LancamentoRequestDto lancamentoRequestDTO, Conta conta, Natureza natureza, TipoLancamento tipoLancamento) {
        Lancamento lancamento = lancamentoService.efetuaLancamento(lancamentoRequestDTO, conta, natureza, tipoLancamento);

        atualizaSaldo(conta, lancamento.getValor(), natureza);

        return lancamento;
    }

    private void atualizaSaldo(Conta conta, BigDecimal valorLancamento, Natureza natureza) {
        BigDecimal saldo = DigitalBankUtils.calculaSaldo(natureza, valorLancamento, conta.getSaldo());

        conta.setSaldo(saldo);
        contaRepository.save(conta);
    }

    private Natureza defineNaturezaPorTipoLancamento(TipoLancamento tipoLancamento) {
        return TipoLancamento.DEPOSITO.equals(tipoLancamento) ? Natureza.CREDITO : Natureza.DEBITO;
    }

    public List<ContaResponseDto> consultaTodas(){
        List<Conta> contas = contaRepository.findAll();
        return contas.stream().map(conta -> entitytoContaResponseDto(conta)).collect(Collectors.toList());
    }

    public ContaResponseDto consultaContaPorIdCliente(Long id){
        Conta conta = contaRepository.findByClienteId(id)
                .orElseThrow(() -> new ServiceException("Conta não encontrada!"));
        return entitytoContaResponseDto(conta);
    }


    public void bloqueiaConta(Long id){
        Conta conta = consultaPorId(id);
        validaBloqueio(conta);
        conta.setSituacao(SituacaoConta.BLOQUEADA);
        contaRepository.save(conta);
    }

    private void validaBloqueio(Conta conta) {
        if(SituacaoConta.BLOQUEADA.getDescricao().equals(conta.getSituacao())){
            throw new ServiceException("Conta de ID "+ conta.getId() + "já se encontra bloqueada.");
        }

    }

    public void desbloqueiaConta(Long id){
        Conta conta = consultaPorId(id);
        validaDesbloqueio(conta);
        conta.setSituacao(SituacaoConta.ABERTA);
        contaRepository.save(conta);

    }

    private void validaDesbloqueio(Conta conta) {
        if(SituacaoConta.ABERTA.getDescricao().equals(conta.getSituacao())){
            throw new ServiceException("Conta de ID " + conta.getId() + "não está bloqueada.");
        }
    }


    public Conta consultaPorId(Long id){
        return contaRepository.findById(id)
                .orElseThrow(() -> new ServiceException("Conta  de id "+ id +"não encontrada"));
    }

    private void validaConta(Cliente cliente) {
        if(contaRepository.findByNumeroConta(cliente.getTelefone()).isPresent()){
            throw new ServiceException("Já existe uma conta com o número de telefone informado!");
        }
    }

    private ContaResponseDto entitytoContaResponseDto(Conta conta) {
        ContaResponseDto contaResponseDto = ContaResponseDto
                .builder()
                    .idCliente(conta.getCliente().getId())
                    .idConta(conta.getId())
                    .numeroAgencia(conta.getNumeroAgencia())
                    .numeroConta(conta.getNumeroConta())
                    .status(SituacaoConta.ABERTA.getDescricao().equals(conta.getSituacao()) ? "A" : "B")
                    .saldo(conta.getSaldo())
                .build();
        return contaResponseDto;
    }

}
