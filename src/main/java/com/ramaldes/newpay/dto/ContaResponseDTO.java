package com.ramaldes.newpay.dto;

import java.math.BigDecimal;

public class ContaResponseDTO {

    private Long id;
    private String numeroConta;
    private BigDecimal saldo;
    private Long clienteId;

    public ContaResponseDTO(Long id, String numeroConta, BigDecimal saldo, Long clienteId) {
        this.id = id;
        this.numeroConta = numeroConta;
        this.saldo = saldo;
        this.clienteId = clienteId;
    }

    public Long getId() {
        return id;
    }

    public String getNumeroConta() {
        return numeroConta;
    }

    public BigDecimal getSaldo() {
        return saldo;
    }

    public Long getClienteId() {
        return clienteId;
    }

}
