package com.ramaldes.newpay.controller;

import com.ramaldes.newpay.dto.ClienteRequestDTO;
import com.ramaldes.newpay.dto.ClienteResponseDTO;
import com.ramaldes.newpay.model.Cliente;
import com.ramaldes.newpay.service.ClienteService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/v1")
public class ClienteController {

    private final ClienteService clienteService;

    public ClienteController(ClienteService clienteService) {
        this.clienteService = clienteService;
    }

    @GetMapping("/teste")
    public String teste() {
        return "Controller funcionando";
    }

    @PostMapping("/clientes")
    public ClienteResponseDTO cadastrarCliente(@Valid @RequestBody ClienteRequestDTO dto) {
        return clienteService.cadastrarCliente(dto);
    }

    @GetMapping("/clientes/{id}")
    public ClienteResponseDTO buscarPorId(@PathVariable Long id) {
        return clienteService.buscarPorId(id);
    }

    @GetMapping("/clientes")
    public List<ClienteResponseDTO> listarClientes() {
        return clienteService.listarClientes();
    }

    @PutMapping("/clientes/{id}")
    public ClienteResponseDTO atualizarCliente(@PathVariable Long id, @Valid @RequestBody ClienteRequestDTO dto) {
        return clienteService.atualizarCliente(id, dto);
    }

    @DeleteMapping("/clientes/{id}")
    public void deletarCliente(@PathVariable Long id) {
         clienteService.deletarCliente(id
         );
    }
}
