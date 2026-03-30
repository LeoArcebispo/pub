package com.grupo_5.pub.Workers;

import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;

@Component
public class WorkerProcessamento {

    @PostConstruct
    public void iniciarWorker() {
        new Thread(() -> {
            while (true) {
                try {
                    String id = WorkerFilaProcessamento.fila.take();

                    System.out.println("Processando: " + id);

                    WorkerProcessamentoStorage.statusMap.put(id, "PROCESSANDO");

                    Thread.sleep(5000);

                    WorkerProcessamentoStorage.statusMap.put(id, "CONCLUIDO");

                    System.out.println("Finalizado: " + id);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}