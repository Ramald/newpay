package com.ramaldes.newpay.service;

import com.ramaldes.newpay.dto.*;
import com.ramaldes.newpay.exception.*;
import com.ramaldes.newpay.model.Cliente;
import com.ramaldes.newpay.model.Conta;
import com.ramaldes.newpay.model.Movimentacao;
import com.ramaldes.newpay.model.TipoOperacao;
import com.ramaldes.newpay.repository.ClienteRepository;
import com.ramaldes.newpay.repository.ContaRepository;
import com.ramaldes.newpay.repository.MovimentacaoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ContaService {

    private final ContaRepository contaRepository;
    private final ClienteRepository clienteRepository;
    private final MovimentacaoRepository movimentacaoRepository;

    public ContaService(ContaRepository contaRepository, ClienteRepository clienteRepository, MovimentacaoRepository movimentacaoRepository) {
        this.contaRepository = contaRepository;
        this.clienteRepository = clienteRepository;
        this.movimentacaoRepository = movimentacaoRepository;
    }

    public ContaResponseDTO abrirConta(Long clienteId) {

        Optional<Cliente> resultado = clienteRepository.findById(clienteId);

        if(resultado.isEmpty()) {
            throw new ClienteNaoEncontradoException("CLIENTE NÃO ENCONTRADO!");
        }

        Cliente cliente = resultado.get();

        boolean contaJaExistente = contaRepository.existsByCliente(cliente);

        if(contaJaExistente) {
            throw new ContaJaExisteException("ESSE CLIENTE JÁ POSSUI UMA CONTA");
        }
        Conta conta = new Conta(
                "NP-" + cliente.getId(),
                BigDecimal.ZERO
        );

        conta.setCliente(cliente);

        Conta contaSalva = contaRepository.save(conta);

        ContaResponseDTO response = new ContaResponseDTO(
                contaSalva.getId(),
                contaSalva.getNumeroConta(),
                contaSalva.getSaldo(),
                contaSalva.getCliente().getId()
                );

        return response;
    }

    @Transactional
    public ContaResponseDTO depositar(Long contaId, DepositoRequestDTO dto) {
        Optional<Conta> buscarConta = contaRepository.findById(contaId);

        if(buscarConta.isEmpty()) {
            throw new ContaNaoEncontradaException("CONTA NÃO ENCONTRADA");
        }

        Conta contaEncontrada = buscarConta.get();

        BigDecimal novoSaldo = contaEncontrada.getSaldo().add(dto.getValor());

        contaEncontrada.setSaldo(novoSaldo);

        Conta contaAtualizada = contaRepository.save(contaEncontrada);

        Movimentacao movimentacao = new Movimentacao(
                TipoOperacao.DEPOSITO,
                dto.getValor(),
                LocalDateTime.now(),
                contaAtualizada
        );
        movimentacaoRepository.save(movimentacao);

        ContaResponseDTO response = new ContaResponseDTO(
                contaAtualizada.getId(),
                contaAtualizada.getNumeroConta(),
                contaAtualizada.getSaldo(),
                contaAtualizada.getCliente().getId()
        );

        return response;
    }

    public List<ContaResponseDTO> listarContas() {
        List<Conta> buscarConta = contaRepository.findAll();
        List<ContaResponseDTO> resposta = new ArrayList<>();

        for(Conta percorrido : buscarConta) {
            ContaResponseDTO dto = new ContaResponseDTO(
                    percorrido.getId(),
                    percorrido.getNumeroConta(),
                    percorrido.getSaldo(),
                    percorrido.getCliente().getId());
            resposta.add(dto);
        }

        return resposta;
    }

    @Transactional
    public ContaResponseDTO sacar(Long contaId, SaqueRequestDTO dto) {
        Optional<Conta> buscarConta = contaRepository.findById(contaId);

        if(buscarConta.isEmpty()) {
            throw new ContaNaoEncontradaException("CONTA NÃO ENCONTRADA");
        }

        Conta contaEncontrada = buscarConta.get();

        int comparacao = contaEncontrada.getSaldo().compareTo(dto.getValor());
        if(comparacao < 0) {
            throw new SaldoInsuficienteException("SALDO INSUFICIENTE.");
        }

        BigDecimal novoSaldo = contaEncontrada.getSaldo().subtract(dto.getValor());

        contaEncontrada.setSaldo(novoSaldo);

        Conta contaAtualizada = contaRepository.save(contaEncontrada);

        Movimentacao movimentacao = new Movimentacao(
                TipoOperacao.SAQUE,
                dto.getValor(),
                LocalDateTime.now(),
                contaAtualizada
        );
        movimentacaoRepository.save(movimentacao);

        ContaResponseDTO response = new ContaResponseDTO(
                contaAtualizada.getId(),
                contaAtualizada.getNumeroConta(),
                contaAtualizada.getSaldo(),
                contaAtualizada.getCliente().getId()
        );

        return response;
    }

    @Transactional
    public TransferenciaResponseDTO transferencia(TransferenciaRequestDTO dto) {

        if(dto.getContaOrigemId().equals(dto.getContaDestinoId())) {
            throw new ContaOrigemEDestinoIguais("A CONTA DE ORIGEM E DESTINO NÃO PODEM SER A MESMA");
        }

        Optional<Conta> buscarContaOrigem = contaRepository.findById(dto.getContaOrigemId());
        Optional<Conta> buscarContaDestino = contaRepository.findById(dto.getContaDestinoId());

        if(buscarContaOrigem.isEmpty()) {
            throw new ContaNaoEncontradaException("CONTA NÃO ENCONTRADA!");
        }

        if(buscarContaDestino.isEmpty()) {
            throw new ContaNaoEncontradaException("CONTA NÃO ENCONTRADA!");
        }

        Conta contaOrigem = buscarContaOrigem.get();
        Conta contaDestino = buscarContaDestino.get();

        int comparacao = contaOrigem.getSaldo().compareTo(dto.getValor());
        if(comparacao < 0) {
            throw new SaldoInsuficienteException("SALDO INSUFICIENTE!");
        }

        BigDecimal novoSaldoOrigem = contaOrigem.getSaldo().subtract(dto.getValor());
        BigDecimal novoSaldoDestino = contaDestino.getSaldo().add(dto.getValor());

        contaOrigem.setSaldo(novoSaldoOrigem);
        contaDestino.setSaldo(novoSaldoDestino);

        Conta contaOrigemAtualizada = contaRepository.save(contaOrigem);
        Conta contaDestinoAtualizada = contaRepository.save(contaDestino);

        LocalDateTime dataHoraTransferencia = LocalDateTime.now();
        Movimentacao movimentacaoSaida = new Movimentacao(
                TipoOperacao.TRANSFERENCIA_SAIDA,
                dto.getValor(),
                dataHoraTransferencia,
                contaOrigemAtualizada
        );
        movimentacaoRepository.save(movimentacaoSaida);

        Movimentacao movimentacaoEntrada = new Movimentacao(
                TipoOperacao.TRANSFERENCIA_ENTRADA,
                dto.getValor(),
                dataHoraTransferencia,
                contaDestinoAtualizada
        );
        movimentacaoRepository.save(movimentacaoEntrada);


        TransferenciaResponseDTO response = new TransferenciaResponseDTO(
                contaOrigemAtualizada.getId(),
                contaDestinoAtualizada.getId(),
                dto.getValor(),
                contaOrigemAtualizada.getSaldo(),
                contaDestinoAtualizada.getSaldo()
        );

        return response;

    }

    public List<ExtratoResponseDTO> extrato(Long contaId) {
        Optional<Conta> buscarConta = contaRepository.findById(contaId);
        if(buscarConta.isEmpty()) {
            throw new ContaNaoEncontradaException("CONTA NÃO ENCONTRADA!");
        }
        Conta conta = buscarConta.get();
        List<Movimentacao> movimentacoes = movimentacaoRepository.findByContaOrderByDataHoraAsc(conta);
        List<ExtratoResponseDTO> respostas = new ArrayList<>();

        for(Movimentacao movimentacao : movimentacoes) {
            ExtratoResponseDTO extrato = new ExtratoResponseDTO(
                    movimentacao.getTipo(),
                    movimentacao.getValor(),
                    movimentacao.getDataHora()
            );
            respostas.add(extrato);
        }

        return respostas;

    }

}
