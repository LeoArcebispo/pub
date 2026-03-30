package com.grupo_5.pub.Controller;

import com.grupo_5.pub.DTO.UploadResponse;
import com.grupo_5.pub.Workers.WorkerFilaProcessamento;
import com.grupo_5.pub.Workers.WorkerProcessamentoStorage;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@RestController
@RequestMapping("/upload")
public class UploadController {

    @PostMapping
    public ResponseEntity<UploadResponse> upload(@RequestParam("file") MultipartFile file) {

        if (file == null || file.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }

        String id = UUID.randomUUID().toString();

        // 🔥 adiciona na fila
        WorkerProcessamentoStorage.statusMap.put(id, "NA_FILA");
        WorkerFilaProcessamento.fila.add(id);

        return ResponseEntity.ok(new UploadResponse(id, "NA_FILA"));
    }
}