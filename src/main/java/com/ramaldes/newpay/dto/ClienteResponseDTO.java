package com.ramaldes.newpay.dto;

import java.time.LocalDate;

public class ClienteResponseDTO {

    private Long id;
    private String nome;
    private String cpf;
    private String email;
    private LocalDate dataNascimento;

    public ClienteResponseDTO(Long id, String nome, String cpf, String email, LocalDate dataNascimento) {
        this.id = id;
        this.nome = nome;
        this.cpf = cpf;
        this.email = email;
        this.dataNascimento = dataNascimento;
    }

    public Long getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public String getCpf() {
        return cpf;
    }

    public String getEmail() {
        return email;
    }

    public LocalDate getDataNascimento() {
        return dataNascimento;
    }

}
