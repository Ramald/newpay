package com.ramaldes.newpay.repository;

import com.ramaldes.newpay.model.Conta;
import com.ramaldes.newpay.model.Movimentacao;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MovimentacaoRepository extends JpaRepository<Movimentacao, Long> {
    Page<Movimentacao> findByContaOrderByDataHoraAsc(Conta conta, Pageable pageable);
}
