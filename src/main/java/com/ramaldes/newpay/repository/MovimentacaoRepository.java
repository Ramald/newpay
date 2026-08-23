package com.ramaldes.newpay.repository;

import com.ramaldes.newpay.model.Conta;
import com.ramaldes.newpay.model.Movimentacao;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MovimentacaoRepository extends JpaRepository<Movimentacao, Long> {
    List<Movimentacao> findByContaOrderByDataHoraAsc(Conta conta);
}
