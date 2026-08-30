package com.ramaldes.newpay.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;


public class ClienteRequestDTO {

    @NotBlank
    private String nome;

    @NotBlank
    private String cpf;

    @NotBlank
    @Email
    private String email;

    @NotNull
    private LocalDate dataNascimento;

    public ClienteRequestDTO() {
    }

    public ClienteRequestDTO(String nome, String cpf, String email, LocalDate dataNascimento) {
        this.nome = nome;
        this.cpf = cpf;
        this.email = email;
        this.dataNascimento = dataNascimento;
    }

    public String getNome(){
        return nome;
    }

    public String getCpf(){
        return cpf;
    }

    public String getEmail(){
        return email;
    }

    public LocalDate getDataNascimento(){
        return dataNascimento;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setDataNascimento(LocalDate dataNascimento) {
        this.dataNascimento = dataNascimento;
    }
}
