package com.ramaldes.newpay.service;

import com.ramaldes.newpay.dto.ContaResponseDTO;
import com.ramaldes.newpay.exception.ClienteNaoEncontradoException;
import com.ramaldes.newpay.exception.ContaJaExisteException;
import com.ramaldes.newpay.model.Cliente;
import com.ramaldes.newpay.model.Conta;
import com.ramaldes.newpay.repository.ClienteRepository;
import com.ramaldes.newpay.repository.ContaRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
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

}
