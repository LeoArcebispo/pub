package com.grupo_5.pub.Config.Async;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

/**
 * Configuração do pool de threads para processamento assíncrono.
 *
 * Habilitamos @EnableAsync aqui para que qualquer método anotado com
 * @Async na aplicação seja executado em uma thread separada, fora
 * do ciclo de vida da requisição HTTP original.
 */
@Configuration
@EnableAsync
public class AsyncConfig implements AsyncConfigurer {

    private static final Logger log = LoggerFactory.getLogger(AsyncConfig.class);

    /**
     * Define o executor padrão para todos os métodos @Async da aplicação.
     *
     * Parâmetros escolhidos para o contexto de um pub (carga moderada):
     *  - corePoolSize(5)  : 5 threads sempre ativas — cobre picos normais de movimento
     *  - maxPoolSize(20)  : expande até 20 em horários de pico (happy hour, eventos)
     *  - queueCapacity(100): fila de até 100 tarefas antes de rejeitar — buffer seguro
     *  - keepAlive(60s)   : threads extras ficam vivas 60 s antes de serem destruídas
     *
     * Se o pool estiver cheio E a fila cheia, o Spring lança
     * RejectedExecutionException — capturamos isso no worker.
     */
    @Bean(name = "webhookTaskExecutor")
    @Override
    public Executor getAsyncExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();

        executor.setCorePoolSize(5);
        executor.setMaxPoolSize(20);
        executor.setQueueCapacity(100);
        executor.setKeepAliveSeconds(60);
        executor.setThreadNamePrefix("webhook-async-");

        // Política de rejeição: loga o erro e descarta a tarefa
        // sem derrubar a thread do chamador (= a requisição HTTP)
        executor.setRejectedExecutionHandler((runnable, pool) ->
            log.error("[AsyncConfig] Pool saturado — tarefa rejeitada. " +
                      "Active threads: {}, Queue size: {}",
                      pool.getActiveCount(), pool.getQueue().size())
        );

        executor.initialize();
        log.info("[AsyncConfig] ThreadPoolTaskExecutor inicializado: core={}, max={}, queue={}",
                 executor.getCorePoolSize(), executor.getMaxPoolSize(), executor.getQueueCapacity());

        return executor;
    }
}