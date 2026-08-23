package com.ramaldes.newpay.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public class TransferenciaRequestDTO {

    @NotNull
    private Long contaOrigemId;

    @NotNull
    private Long contaDestinoId;

    @NotNull
    @Positive
    private BigDecimal valor;

    public TransferenciaRequestDTO() {}

    public TransferenciaRequestDTO(Long contaOrigemId, Long contaDestinoId, BigDecimal valor) {
        this.contaOrigemId = contaOrigemId;
        this.contaDestinoId = contaDestinoId;
        this.valor = valor;
    }

    public Long getContaOrigemId() {
        return contaOrigemId;
    }

    public Long getContaDestinoId(){
        return contaDestinoId;
    }

    public BigDecimal getValor() {
        return valor;
    }

    public void setContaOrigemId(Long contaOrigemId) {
        this.contaOrigemId = contaOrigemId;
    }

    public void setContaDestinoId(Long contaDestinoId) {
        this.contaDestinoId = contaDestinoId;
    }

    public void setValor(BigDecimal valor) {
        this.valor = valor;
    }

}
