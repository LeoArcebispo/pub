package com.grupo_5.pub.DTO;

public class UploadResponse {

    private String id;
    private String status;

    public UploadResponse(String id, String status) {
        this.id = id;
        this.status = status;
    }

    public String getId() {
        return id;
    }

    public String getStatus() {
        return status;
    }
}