package com.ramaldes.newpay.service;

import com.ramaldes.newpay.dto.ClienteResponseDTO;
import com.ramaldes.newpay.dto.ContaResponseDTO;
import com.ramaldes.newpay.dto.DepositoRequestDTO;
import com.ramaldes.newpay.exception.ClienteNaoEncontradoException;
import com.ramaldes.newpay.exception.ContaJaExisteException;
import com.ramaldes.newpay.exception.ContaNaoEncontradaException;
import com.ramaldes.newpay.model.Cliente;
import com.ramaldes.newpay.model.Conta;
import com.ramaldes.newpay.repository.ClienteRepository;
import com.ramaldes.newpay.repository.ContaRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ContaService {

    private final ContaRepository contaRepository;
    private final ClienteRepository clienteRepository;

    public ContaService(ContaRepository contaRepository, ClienteRepository clienteRepository) {
        this.contaRepository = contaRepository;
        this.clienteRepository = clienteRepository;
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

    public ContaResponseDTO depositar(Long contaId, DepositoRequestDTO dto) {
        Optional<Conta> buscarConta = contaRepository.findById(contaId);

        if(buscarConta.isEmpty()) {
            throw new ContaNaoEncontradaException("CONTA NÃO ENCONTRADA");
        }

        Conta contaEncontrada = buscarConta.get();

        BigDecimal novoSaldo = contaEncontrada.getSaldo().add(dto.getValor());

        contaEncontrada.setSaldo(novoSaldo);

        Conta contaAtualizada = contaRepository.save(contaEncontrada);

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

}
