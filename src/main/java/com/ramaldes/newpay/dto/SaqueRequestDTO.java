package com.ramaldes.newpay.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public class SaqueRequestDTO {

    @NotNull
    @Positive
    private BigDecimal valor;

    public SaqueRequestDTO() {}

    public SaqueRequestDTO(BigDecimal valor) {
        this.valor = valor;
    }

    public BigDecimal getValor() {
        return valor;
    }

    public void setValor(BigDecimal valor) {
        this.valor = valor;
    }
}
