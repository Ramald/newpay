package com.ramaldes.newpay.controller;

import com.ramaldes.newpay.dto.*;
import com.ramaldes.newpay.model.Conta;
import com.ramaldes.newpay.service.ContaService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1")
public class ContaController {

    private final ContaService contaService;

    public ContaController(ContaService contaService) {
        this.contaService = contaService;
    }



    @GetMapping("/contas")
    public List<ContaResponseDTO> listarContas() {
        return contaService.listarContas();
    }

    @GetMapping("/contas/{contaId}/extrato")
    public Page<ExtratoResponseDTO> extrato(@PathVariable Long contaId, @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) {
        return contaService.extrato(contaId, page, size);
    }

    @PostMapping("/contas/{clienteId}")
    public ContaResponseDTO abrirConta(@PathVariable Long clienteId){
        return contaService.abrirConta(clienteId);
    }

    @PostMapping("/contas/{contaId}/depositos")
    public ContaResponseDTO depositar(@PathVariable Long contaId, @Valid @RequestBody DepositoRequestDTO dto){
        return contaService.depositar(contaId, dto);
    }

    @PostMapping("/contas/{contaId}/saques")
    public ContaResponseDTO sacar(@PathVariable Long contaId, @Valid @RequestBody SaqueRequestDTO dto) {
        return contaService.sacar(contaId, dto);
    }

    @PostMapping("/contas/transferencias")
    public TransferenciaResponseDTO transferencia(@Valid @RequestBody TransferenciaRequestDTO dto) {
        return contaService.transferencia(dto);
    }



}
