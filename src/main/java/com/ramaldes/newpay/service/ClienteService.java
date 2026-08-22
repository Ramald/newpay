package com.ramaldes.newpay.service;

import com.ramaldes.newpay.dto.ClienteRequestDTO;
import com.ramaldes.newpay.dto.ClienteResponseDTO;
import com.ramaldes.newpay.exception.ClienteNaoEncontradoException;
import com.ramaldes.newpay.exception.CpfJaCadastradoException;
import com.ramaldes.newpay.exception.EmailJaCadastradoException;
import com.ramaldes.newpay.model.Cliente;
import com.ramaldes.newpay.repository.ClienteRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ClienteService {

    private final ClienteRepository clienteRepository;

    public ClienteService(ClienteRepository clienteRepository) {
        this.clienteRepository = clienteRepository;
    }

    //-------------------------------------------------------------------------------------------
    public ClienteResponseDTO cadastrarCliente(ClienteRequestDTO dto) {

        boolean cpfExistente = clienteRepository.existsByCpf(dto.getCpf());
        boolean emailExistente = clienteRepository.existsByEmail(dto.getEmail());

        if (cpfExistente) {
            throw new CpfJaCadastradoException("CPF JÁ CADASTRADO!");
        }

        if (emailExistente) {
            throw new EmailJaCadastradoException("E-MAIL JÁ CADASTRADO");
        }

        Cliente cliente = new Cliente(
                dto.getNome(),
                dto.getCpf(),
                dto.getEmail(),
                dto.getDataNascimento()
        );

        Cliente clienteSalvo = clienteRepository.save(cliente);

        ClienteResponseDTO response = new ClienteResponseDTO(
                clienteSalvo.getId(),
                clienteSalvo.getNome(),
                clienteSalvo.getCpf(),
                clienteSalvo.getEmail(),
                clienteSalvo.getDataNascimento());

        return response;
    }

//-------------------------------------------------------------------------------------------
    public ClienteResponseDTO buscarPorId(Long id) {

        Optional<Cliente> resultado = clienteRepository.findById(id);

        if (resultado.isEmpty()) {
            throw new ClienteNaoEncontradoException("CLIENTE NÃO ENCONTRADO!");
        }
        Cliente cliente = resultado.get();
        return new ClienteResponseDTO(
                cliente.getId(),
                cliente.getNome(),
                cliente.getCpf(),
                cliente.getEmail(),
                cliente.getDataNascimento());
    }
    //-------------------------------------------------------------------------------------------
    public List<ClienteResponseDTO> listarClientes() {
        List<Cliente> resultado = clienteRepository.findAll();

        List<ClienteResponseDTO> resposta = new ArrayList<>();

        for(Cliente percorrido : resultado) {

           ClienteResponseDTO dto = new ClienteResponseDTO(
                   percorrido.getId(),
                   percorrido.getNome(),
                   percorrido.getCpf(),
                   percorrido.getEmail(),
                   percorrido.getDataNascimento());
            resposta.add(dto);
        }

        return resposta;
    }
    //-------------------------------------------------------------------------------------------

    public ClienteResponseDTO atualizarCliente(Long id, ClienteRequestDTO dto) {
        Optional<Cliente> cliente = clienteRepository.findById(id);

        if(cliente.isEmpty()) {
            throw new ClienteNaoEncontradoException("CLIENTE NÃO ENCONTRADO!");
        }

        Cliente clienteExistente = cliente.get();

        boolean cpfPertenceAOutroCliente = clienteRepository.existsByCpfAndIdNot(dto.getCpf(), id);
        boolean emailPertenceAOutroCliente = clienteRepository.existsByEmailAndIdNot(dto.getEmail(), id);

        if(cpfPertenceAOutroCliente) {
            throw new CpfJaCadastradoException("ESSE CPF PERTENCE A OUTRO CLIENTE");
        }
        if(emailPertenceAOutroCliente) {
            throw new EmailJaCadastradoException("ESSE E-MAIL PERTENCE A OUTRO CLIENTE");
        }

        clienteExistente.setNome(dto.getNome());
        clienteExistente.setCpf(dto.getCpf());
        clienteExistente.setEmail(dto.getEmail());
        clienteExistente.setDataNascimento(dto.getDataNascimento());

        Cliente clienteAtualizado = clienteRepository.save(clienteExistente);

        ClienteResponseDTO clienteResponseDTO = new ClienteResponseDTO(
                clienteAtualizado.getId(),
                clienteAtualizado.getNome(),
                clienteAtualizado.getCpf(),
                clienteAtualizado.getEmail(),
                clienteAtualizado.getDataNascimento());

        return clienteResponseDTO;
    }

    public void deletarCliente(Long id) {
        Optional<Cliente> cliente = clienteRepository.findById(id);

        if(cliente.isEmpty()) {
            throw new ClienteNaoEncontradoException("CLIENTE NÃO ENCONTRADO!");
        }

        Cliente clienteExistente = cliente.get();

        clienteRepository.delete(clienteExistente);

    }

}
