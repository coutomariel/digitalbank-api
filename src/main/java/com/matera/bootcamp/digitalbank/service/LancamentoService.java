package com.matera.bootcamp.digitalbank.service;

import com.matera.bootcamp.digitalbank.dto.request.LancamentoRequestDto;
import com.matera.bootcamp.digitalbank.dto.response.ComprovantesResponseDto;
import com.matera.bootcamp.digitalbank.entity.Conta;
import com.matera.bootcamp.digitalbank.entity.Estorno;
import com.matera.bootcamp.digitalbank.entity.Lancamento;
import com.matera.bootcamp.digitalbank.entity.Transferencia;
import com.matera.bootcamp.digitalbank.enumerator.Natureza;
import com.matera.bootcamp.digitalbank.enumerator.SituacaoConta;
import com.matera.bootcamp.digitalbank.enumerator.TipoLancamento;
import com.matera.bootcamp.digitalbank.exception.ServiceException;
import com.matera.bootcamp.digitalbank.repository.EstornoRepository;
import com.matera.bootcamp.digitalbank.repository.LancamentoRepository;
import com.matera.bootcamp.digitalbank.repository.TransferenciaRepository;
import com.matera.bootcamp.digitalbank.utils.DigitalBankUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class LancamentoService {

    private final LancamentoRepository lancamentoRepository;
    private final TransferenciaRepository transferenciaRepository;
    private final EstornoRepository estornoRepository;

    public LancamentoService(LancamentoRepository lancamentoRepository, TransferenciaRepository transferenciaRepository, EstornoRepository estornoRepository) {
        this.lancamentoRepository = lancamentoRepository;
        this.transferenciaRepository = transferenciaRepository;
        this.estornoRepository = estornoRepository;
    }

    @Transactional
    public Lancamento efetuaLancamento(LancamentoRequestDto lancamentoRequestDTO, Conta conta, Natureza natureza, TipoLancamento tipoLancamento) {
        Lancamento lancamento = Lancamento
                .builder()
                    .dataHora(LocalDateTime.now())
                    .codigoAutenticacao(geraAutenticacao())
                    .valor(lancamentoRequestDTO.getValor())
                    .natureza(natureza.getCodigo())
                    .tipoLancamento(tipoLancamento.getCodigo())
                    .descricao(lancamentoRequestDTO.getDescricao())
                    .conta(conta)
                .build();

        validaLancamento(lancamento);

        return lancamentoRepository.save(lancamento);
    }

    @Transactional
    public ComprovantesResponseDto efetuaTransferencia(Lancamento lancamentoDebito, Lancamento lancamentoCredito) {
        Transferencia transferencia = new Transferencia(lancamentoDebito, lancamentoCredito);

        transferenciaRepository.save(transferencia);

        return entidadeParaComprovanteResponseDTO(lancamentoDebito);
    }

    public List<ComprovantesResponseDto> consultaExtratoCompleto(Conta conta) {
        List<Lancamento> lancamentos = lancamentoRepository.findByConta_IdOrderByIdDesc(conta.getId());

        List<ComprovantesResponseDto> comprovantesResponseDto = new ArrayList<>();
        lancamentos.forEach(l -> comprovantesResponseDto.add(entidadeParaComprovanteResponseDTO(l)));

        return comprovantesResponseDto;
    }

    public List<ComprovantesResponseDto> consultaExtratoPorPeriodo(LocalDate dataInicial, LocalDate dataFinal) {
        List<Lancamento> lancamentos = lancamentoRepository.consultaLancamentosPorPeriodo(dataInicial, dataFinal);

        List<ComprovantesResponseDto> comprovantesResponseDTO = new ArrayList<>();
        lancamentos.forEach(l -> comprovantesResponseDTO.add(entidadeParaComprovanteResponseDTO(l)));

        return comprovantesResponseDTO;
    }

    @Transactional
    public ComprovantesResponseDto estornaLancamento(Long idConta, Long idLancamento) {
        Lancamento lancamento = lancamentoRepository.findByIdAndConta_Id(idLancamento, idConta).orElse(null);
        Transferencia transferencia = transferenciaRepository.consultaTransferenciaPorIdLancamento(idLancamento).orElse(null);

        validaEstorno(lancamento, transferencia, idConta, idLancamento);

        if (transferencia != null) {
            return trataEstornoTransferencia(transferencia);
        } else {
            return trataEstornoLancamento(lancamento);
        }
    }

    public ComprovantesResponseDto consultaComprovanteLancamento(Long idConta, Long idLancamento) {
        Lancamento lancamento = lancamentoRepository.findByIdAndConta_Id(idLancamento, idConta)
                .orElseThrow(() -> new ServiceException("O lançamento de ID " + idLancamento + " não existe para a conta de ID " + idConta + "."));

        return entidadeParaComprovanteResponseDTO(lancamento);
    }

    public ComprovantesResponseDto entidadeParaComprovanteResponseDTO(Lancamento lancamento) {
        return ComprovantesResponseDto.builder().idLancamento(lancamento.getId())
                .codigoAutenticacao(lancamento.getCodigoAutenticacao())
                .dataHora(lancamento.getDataHora())
                .valor(lancamento.getValor())
                .natureza(lancamento.getNatureza())
                .tipoLancamento(lancamento.getTipoLancamento())
                .descricao(lancamento.getDescricao())
                .build();
    }

    private void validaLancamento(Lancamento lancamento) {
        if (SituacaoConta.BLOQUEADA.getDescricao().equals(lancamento.getConta().getSituacao())) {
            throw new ServiceException("Conta de ID " + lancamento.getConta().getId() + " está na situação Bloqueada. Novos lançamentos não são permitidos.");
        }

        if (Natureza.DEBITO.getCodigo().equals(lancamento.getNatureza()) && lancamento.getConta().getSaldo().compareTo(lancamento.getValor()) < 0) {
            throw new ServiceException("Saldo indisponível para efetuar o lançamento.");
        }
    }

    private void validaEstorno(Lancamento lancamento, Transferencia transferencia, Long idConta, Long idLancamento) {
        if (lancamento == null) {
            throw new ServiceException("O lançamento de ID " + idLancamento + " não existe para a conta de ID " + idConta + ".");
        }

        if (TipoLancamento.ESTORNO.getCodigo().equals(lancamento.getTipoLancamento())) {
            throw new ServiceException("Tipo de lançamento " + lancamento.getTipoLancamento() + " não permite estornos.");
        }

        if (estornoRepository.findByLancamentoOriginal_Id(lancamento.getId()).isPresent()) {
            throw new ServiceException("O lançamento informado já está estornado.");
        }

        if (TipoLancamento.TRANSFERENCIA.getCodigo().equals(lancamento.getTipoLancamento()) && !lancamento.getId().equals(transferencia.getLancamentoCredito().getId())) {
            throw new ServiceException("Estorno de transferência só pode ser solicitado pela conta creditada.");
        }

        if (SituacaoConta.BLOQUEADA.getDescricao().equals(lancamento.getConta().getSituacao())) {
            throw new ServiceException("Conta de ID " + lancamento.getConta().getId() + " está na situação Bloqueada. Novos lançamentos não são permitidos.");
        }

        if (Natureza.CREDITO.getCodigo().equals(lancamento.getNatureza()) && lancamento.getConta().getSaldo().compareTo(lancamento.getValor()) < 0) {
            throw new ServiceException("Saldo indisponível para estornar o lançamento de crédito.");
        }
    }

    private ComprovantesResponseDto trataEstornoTransferencia(Transferencia transferencia) {
        trataEstornoLancamento(transferencia.getLancamentoDebito());
        return trataEstornoLancamento(transferencia.getLancamentoCredito());
    }

    private ComprovantesResponseDto trataEstornoLancamento(Lancamento lancamento) {
        Conta conta = lancamento.getConta();
        Natureza natureza = defineNaturezaEstorno(lancamento);
        conta.setSaldo(DigitalBankUtils.calculaSaldo(natureza, lancamento.getValor(), conta.getSaldo()));

        Lancamento lancamentoEstorno = Lancamento.builder().codigoAutenticacao(geraAutenticacao())
                .conta(conta)
                .dataHora(LocalDateTime.now())
                .descricao("Estorno do lançamento " + lancamento.getId())
                .natureza(natureza.getCodigo())
                .tipoLancamento(TipoLancamento.ESTORNO.getCodigo())
                .valor(lancamento.getValor())
                .build();

        lancamento.setDescricao(lancamento.getDescricao() + " - Estornado");
        lancamentoRepository.save(lancamento);
        lancamentoRepository.save(lancamentoEstorno);

        Estorno estorno = Estorno.builder().lancamentoEstorno(lancamentoEstorno)
                .lancamentoOriginal(lancamento)
                .build();

        estornoRepository.save(estorno);

        return entidadeParaComprovanteResponseDTO(lancamentoEstorno);
    }

    private String geraAutenticacao() {
        return UUID.randomUUID().toString();
    }

    private Natureza defineNaturezaEstorno(Lancamento lancamento) {
        return Natureza.DEBITO.getCodigo().equals(lancamento.getNatureza()) ? Natureza.CREDITO : Natureza.DEBITO;
    }

}