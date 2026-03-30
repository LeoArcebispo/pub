package com.grupo_5.pub.jobs;

import com.grupo_5.pub.Model.Sessao;
import com.grupo_5.pub.Repository.SessaoRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
public class SessaoCleanupJob {

    private final SessaoRepository sessaoRepository;

    public SessaoCleanupJob(SessaoRepository sessaoRepository) {
        this.sessaoRepository = sessaoRepository;
    }

    @Scheduled(fixedRate = 60000)
    public void limparSessoesExpiradas() {

        List<Sessao> expiradas = sessaoRepository
                .findByExpiraEmBefore(LocalDateTime.now());

        if (!expiradas.isEmpty()) {
            sessaoRepository.deleteAll(expiradas);
            System.out.println("🧹 Sessões removidas: " + expiradas.size());
        }
    }


}