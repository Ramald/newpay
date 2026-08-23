package com.ramaldes.newpay.dto;

import com.ramaldes.newpay.model.TipoOperacao;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class ExtratoResponseDTO {

    private TipoOperacao tipo;
    private BigDecimal valor;
    private LocalDateTime dataHora;

    public ExtratoResponseDTO(TipoOperacao tipo, BigDecimal valor, LocalDateTime dataHora) {
        this.tipo = tipo;
        this.valor = valor;
        this.dataHora = dataHora;
    }

    public TipoOperacao getTipo() {
        return tipo;
    }

    public BigDecimal getValor() {
        return valor;
    }

    public LocalDateTime getHora() {
        return dataHora;
    }
}
