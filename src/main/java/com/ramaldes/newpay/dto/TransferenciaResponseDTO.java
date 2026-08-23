package com.ramaldes.newpay.dto;

import java.math.BigDecimal;

public class TransferenciaResponseDTO {

    private Long contaOrigemId;
    private Long contaDestinoId;
    private BigDecimal valor;
    private BigDecimal saldoContaOrigem;
    private BigDecimal saldoContaDestino;

    public TransferenciaResponseDTO(Long contaOrigemId, Long contaDestinoId, BigDecimal valor, BigDecimal saldoContaOrigem, BigDecimal saldoContaDestino) {
        this.contaOrigemId = contaOrigemId;
        this.contaDestinoId = contaDestinoId;
        this.valor = valor;
        this.saldoContaOrigem = saldoContaOrigem;
        this.saldoContaDestino = saldoContaDestino;
    }

    public Long getContaOrigemId() {
        return contaOrigemId;
    }

    public Long getContaDestinoId() {
        return contaDestinoId;
    }

    public BigDecimal getValor() {
        return valor;
    }

    public BigDecimal getSaldoContaOrigem() {
        return saldoContaOrigem;
    }

    public BigDecimal getSaldoContaDestino() {
        return saldoContaDestino;
    }
}
