package com.ramaldes.newpay.model;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
public class Movimentacao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Enumerated(EnumType.STRING)
    private TipoOperacao tipo;
    private BigDecimal valor;
    private LocalDateTime dataHora;
    @ManyToOne
    @JoinColumn(name = "conta_id")
    private Conta conta;

    public Movimentacao(){}

    public Movimentacao(TipoOperacao tipo, BigDecimal valor, LocalDateTime dataHora, Conta conta) {
        this.tipo = tipo;
        this.valor = valor;
        this.dataHora = dataHora;
        this.conta = conta;
    }

    public Long getId() {
        return id;
    }

    public TipoOperacao getTipo() {
        return tipo;
    }

    public BigDecimal getValor() {
        return valor;
    }

    public LocalDateTime getDataHora() {
        return dataHora;
    }

    public Conta getConta() {
        return conta;
    }
}
