package com.ramaldes.newpay;

import com.ramaldes.newpay.dto.*;
import com.ramaldes.newpay.exception.ContaNaoEncontradaException;
import com.ramaldes.newpay.exception.ContaOrigemEDestinoIguais;
import com.ramaldes.newpay.exception.SaldoInsuficienteException;
import com.ramaldes.newpay.model.Cliente;
import com.ramaldes.newpay.model.Conta;
import com.ramaldes.newpay.model.Movimentacao;
import com.ramaldes.newpay.model.TipoOperacao;
import com.ramaldes.newpay.repository.ClienteRepository;
import com.ramaldes.newpay.repository.ContaRepository;
import com.ramaldes.newpay.repository.MovimentacaoRepository;
import com.ramaldes.newpay.service.ContaService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ContaServiceTest {

    @Mock
    ClienteRepository clienteRepository;

    @Mock
    ContaRepository contaRepository;

    @Mock
    MovimentacaoRepository movimentacaoRepository;

    @InjectMocks
    ContaService contaService;

    @Test
    void deveSacar100DeUmaContaComSaldo500() {

        Cliente cliente = new Cliente("Teste", "123456789", "teste@email.com", LocalDate.of(2000, 1, 1));

        Conta conta = new Conta("NP-1", new BigDecimal("500.00"));

        conta.setCliente(cliente);

        SaqueRequestDTO dto = new SaqueRequestDTO(new BigDecimal("100.00"));

        when(contaRepository.findById(1L)).thenReturn(Optional.of(conta));
        when(contaRepository.save(conta)).thenReturn(conta);

        ContaResponseDTO resultado = contaService.sacar(1L, dto);

        assertEquals(new BigDecimal("400.00"), resultado.getSaldo());
    }

    @Test
    void deveLancarExceptionQuandoSaldoForInsuficiente() {
        Conta conta = new Conta("NP-1", new BigDecimal("100.00"));

        SaqueRequestDTO dto = new SaqueRequestDTO(new BigDecimal("150.00"));

        when(contaRepository.findById(1L)).thenReturn(Optional.of(conta));

        assertThrows(SaldoInsuficienteException.class, () -> contaService.sacar(1L, dto));
    }

    @Test
    void deveTransferirSaldoOrigemParaDestino() {
        Conta contaOrigem = new Conta("NP-1", new BigDecimal("500.00"));

        Conta contaDestino = new Conta("NP-2", new BigDecimal("700.00"));

        when(contaRepository.findById(1L)).thenReturn(Optional.of(contaOrigem));
        when(contaRepository.findById(2L)).thenReturn(Optional.of(contaDestino));

        when(contaRepository.save(contaOrigem)).thenReturn(contaOrigem);
        when(contaRepository.save(contaDestino)).thenReturn(contaDestino);

        TransferenciaRequestDTO transfDTO = new TransferenciaRequestDTO(1L, 2L, new BigDecimal("100.00"));

        TransferenciaResponseDTO resultado = contaService.transferencia(transfDTO);

        assertEquals(new BigDecimal("400.00"), resultado.getSaldoContaOrigem());
        assertEquals(new BigDecimal("800.00"), resultado.getSaldoContaDestino());
    }

    @Test
    void deveLancarExceptionQuandoODForemIguais() {

        TransferenciaRequestDTO dto = new TransferenciaRequestDTO(1L, 1L, new BigDecimal("100.00"));

        assertThrows(ContaOrigemEDestinoIguais.class, () -> contaService.transferencia(dto));

    }

    @Test
    void deveLancarExceptionQuandoSaldoForInsuficienteNaTransferencia() {
        Conta contaOrigem = new Conta("NP-1", new BigDecimal("100.00"));

        Conta contaDestino = new Conta("NP-2", new BigDecimal("200.00"));

        when(contaRepository.findById(1L)).thenReturn(Optional.of(contaOrigem));
        when(contaRepository.findById(2L)).thenReturn(Optional.of(contaDestino));

        TransferenciaRequestDTO dto = new TransferenciaRequestDTO(1L, 2L, new BigDecimal("150.00"));

        assertThrows(SaldoInsuficienteException.class, () -> contaService.transferencia(dto));

    }

    @Test
    void deveLancarExceptionSeContaOrigemNaoExistir() {
        TransferenciaRequestDTO dto = new TransferenciaRequestDTO(1L, 2L, new BigDecimal("150.00"));

        when(contaRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ContaNaoEncontradaException.class, () -> contaService.transferencia(dto));
    }

    @Test
    void deveLancarExceptionSeContaDestinoNaoExistir() {
        Conta contaOrigem = new Conta("NP-1", new BigDecimal("100.00"));
        TransferenciaRequestDTO dto = new TransferenciaRequestDTO(1L, 2L, new BigDecimal("150.00"));

        when(contaRepository.findById(1L)).thenReturn(Optional.of(contaOrigem));
        when(contaRepository.findById(2L)).thenReturn(Optional.empty());

        assertThrows(ContaNaoEncontradaException.class, () -> contaService.transferencia(dto));
    }

    @Test
    void deveSomarValorAoSaldoQuandoDepositado() {
        Conta conta = new Conta("NP-100", new BigDecimal("200.00"));

        Cliente cliente = new Cliente("Teste", "12345678900", "teste@email.com", LocalDate.of(2000, 1, 1));

        conta.setCliente(cliente);

        DepositoRequestDTO dto = new DepositoRequestDTO(new BigDecimal("50.00"));

        when(contaRepository.findById(1L)).thenReturn(Optional.of(conta));
        when(contaRepository.save(conta)).thenReturn(conta);

        ContaResponseDTO resultado = contaService.depositar(1L, dto);

        assertEquals(new BigDecimal("250.00"), resultado.getSaldo());

        ArgumentCaptor<Movimentacao> captor = ArgumentCaptor.forClass(Movimentacao.class);

        verify(movimentacaoRepository).save(captor.capture());
        Movimentacao movimentacaoSalva = captor.getValue();

        assertEquals(new BigDecimal("50.00"), movimentacaoSalva.getValor());
        assertEquals(TipoOperacao.DEPOSITO, movimentacaoSalva.getTipo());
    }

    @Test
    void deveDepositarValorNaConta() {
        //GIVEN
        Conta conta = new Conta("NP-1", new BigDecimal("500.00"));
        DepositoRequestDTO dto = new DepositoRequestDTO(new BigDecimal("100.00"));

        Cliente cliente = new Cliente("nome", "12130130103", "email@email.com", LocalDate.of(2000, 01, 01));

        conta.setCliente(cliente);


        when(contaRepository.findById(1L)).thenReturn(Optional.of(conta));
        when(contaRepository.save(conta)).thenReturn(conta);

        ContaResponseDTO resultado = contaService.depositar(1L, dto);

        assertEquals(new BigDecimal("600.00"), resultado.getSaldo());
    }



    // Método: sacar(Long contaId, SaqueRequestDTO dto)
    // Entra: contaId + SaqueRequestDTO
    // Sai: ContaResponseDTO
    @Test
    void deveDiminuirSaldoQuandoSacarValorValido() {
        //GIVEN
        Conta conta = new Conta("NP-100", new BigDecimal("500.00"));
        Cliente cliente = new Cliente("nome", "12130130103", "email@email.com", LocalDate.of(2000, 1, 1));
        conta.setCliente(cliente);
        SaqueRequestDTO dto = new SaqueRequestDTO(
                new BigDecimal("100.00")
        );

        when(contaRepository.findById(1L))
                .thenReturn(Optional.of(conta));
        when(contaRepository.save(conta))
                .thenReturn(conta);

        //WHEN
        ContaResponseDTO resultado = contaService.sacar(1L, dto);

        //THEN
        assertEquals(new BigDecimal("400.00"), resultado.getSaldo());
    }


    // Método: sacar(Long contaId, SaqueRequestDTO dto)
    // Entra: contaId + SaqueRequestDTO
    // Sai: ContaResponseDTO se der certo
    // Mas neste teste esperamos exception
    @Test
    void deveLancarExceptionQuandoSaldoInsuficiente() {
        //GIVEN
        Conta conta = new Conta("NP-100", new BigDecimal("100.00"));
        SaqueRequestDTO dto = new SaqueRequestDTO(
                new BigDecimal("150.00")
        );

        when(contaRepository.findById(1L))
                .thenReturn(Optional.of(conta));
        //WHEN/THEN
        assertThrows(SaldoInsuficienteException.class,
                () -> contaService.sacar(1L, dto)
                );
    }

    @Test
    void deveLancarExceptionQuandoContaNaoEncontrada(){

        SaqueRequestDTO dto = new SaqueRequestDTO(new BigDecimal("100.00"));
        when(contaRepository.findById(1L))
                .thenReturn(Optional.empty());

        assertThrows(ContaNaoEncontradaException.class,
                () -> contaService.sacar(1L, dto));

    }

    @Test
    void deveLancarExceptionQuandoDepositoComContaNaoEncontrada() {
        DepositoRequestDTO dto = new DepositoRequestDTO(new BigDecimal("100.00"));

        when(contaRepository.findById(1L))
                .thenReturn(Optional.empty());

        assertThrows(ContaNaoEncontradaException.class,
                () -> contaService.depositar(1L, dto)
                );
    }

    @Test
    void deveVerificarSeSaveFoiChamadoDuasVezes() {

        Conta contaOrigem = new Conta(
                "NP-1",
                new BigDecimal("500.00")
        );
        Conta contaDestino = new Conta(
                "NP-2",
                new BigDecimal("200.00")
        );
        TransferenciaRequestDTO dto = new TransferenciaRequestDTO(
                1L,
                2L,
                new BigDecimal("100.00")
        );

        when(contaRepository.findById(1L)).
                thenReturn(Optional.of(contaOrigem));
        when(contaRepository.findById(2L))
                .thenReturn(Optional.of(contaDestino));

        when(contaRepository.save(contaOrigem))
                .thenReturn(contaOrigem);
        when(contaRepository.save(contaDestino))
                .thenReturn(contaDestino);

        contaService.transferencia(dto);

        ArgumentCaptor<Movimentacao> captor = ArgumentCaptor.forClass(Movimentacao.class);

        verify(movimentacaoRepository, times(2))
                .save(captor.capture());

        List<Movimentacao> movimentacaosCapturadas = captor.getAllValues();

        assertEquals(
                TipoOperacao.TRANSFERENCIA_SAIDA,
                movimentacaosCapturadas.get(0).getTipo()
                );
        assertEquals(
                TipoOperacao.TRANSFERENCIA_ENTRADA,
                movimentacaosCapturadas.get(1).getTipo()
                );
    }

}
