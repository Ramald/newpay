package com.ramaldes.newpay.repository;

import com.ramaldes.newpay.model.Cliente;
import com.ramaldes.newpay.model.Conta;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ContaRepository extends JpaRepository<Conta, Long> {

    boolean existsByCliente(Cliente cliente);

}
