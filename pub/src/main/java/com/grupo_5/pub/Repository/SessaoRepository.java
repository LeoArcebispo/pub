package com.grupo_5.pub.Repository;

import com.grupo_5.pub.Model.Sessao;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface SessaoRepository extends JpaRepository<Sessao, Integer> {
    List<Sessao> findByExpiraEmBefore(LocalDateTime data);
}
