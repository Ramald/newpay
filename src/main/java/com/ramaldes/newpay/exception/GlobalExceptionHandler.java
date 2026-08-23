package com.ramaldes.newpay.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler  {

    @ExceptionHandler(CpfJaCadastradoException.class)
    public ResponseEntity<String> TratarCpfJaCadastrado(CpfJaCadastradoException exception) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(exception.getMessage());
    }

    @ExceptionHandler(EmailJaCadastradoException.class)
    public ResponseEntity<String> TratarEmailJaCadastrado(EmailJaCadastradoException exception) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(exception.getMessage());
    }

    @ExceptionHandler(ClienteNaoEncontradoException.class)
    public ResponseEntity<String> TratarClienteNaoEncontrado(ClienteNaoEncontradoException exception) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(exception.getMessage());
    }

    @ExceptionHandler(ContaJaExisteException.class)
    public ResponseEntity<String> TratarContaJaExiste(ContaJaExisteException exception) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(exception.getMessage());
    }

    @ExceptionHandler(ContaNaoEncontradaException.class)
    public ResponseEntity<String> TratarContaNaoEncontrada(ContaNaoEncontradaException exception) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(exception.getMessage());
    }

    @ExceptionHandler(SaldoInsuficienteException.class)
    public ResponseEntity<String> TratarSaldoInsuficiente(SaldoInsuficienteException exception) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exception.getMessage());
    }

    @ExceptionHandler(ContaOrigemEDestinoIguais.class)
    public ResponseEntity<String> TratarContaOrigemEDestinoIguais(ContaOrigemEDestinoIguais exception) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exception.getMessage());
    }
}
