package com.grupo_5.pub.Workers;

import com.grupo_5.pub.Fila.FilaEmail;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;

@Component
public class EmailWorker {

    @PostConstruct
    public void iniciarWorker() {
        new Thread(() -> {
            while (true) {
                try {
                    String mensagem = FilaEmail.fila.take();

                    System.out.println("📧 Enviando email de boas-vindas...");
                    Thread.sleep(3000);

                    System.out.println("✅ Email enviado: " + mensagem);

                } catch (Exception e) {
                    System.out.println("❌ Erro ao processar fila");
                }
            }
        }).start();
    }
}