package com.ramaldes.newpay.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public class DepositoRequestDTO {

    @NotNull
    @Positive
    private BigDecimal valor;

    public DepositoRequestDTO(){}

    public DepositoRequestDTO(BigDecimal valor) {
        this.valor = valor;
    }

    public BigDecimal getValor() {
        return valor;
    }

    public void setValor(BigDecimal valor) {
        this.valor = valor;
    }
}
