package com.ramaldes.newpay.controller;

import com.ramaldes.newpay.dto.ContaResponseDTO;
import com.ramaldes.newpay.service.ContaService;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1")
public class ContaController {

    private final ContaService contaService;

    public ContaController(ContaService contaService) {
        this.contaService = contaService;
    }

    @PostMapping("/contas/{clienteId}")
    public ContaResponseDTO abrirConta(@PathVariable Long clienteId){
        return contaService.abrirConta(clienteId);
    }
}
