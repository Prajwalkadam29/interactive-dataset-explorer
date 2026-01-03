package com.praj.datasetai.dto;

import lombok.Data;

@Data
public class ChatRequest {
    private String sessionId; // Frontend will generate and send this (e.g., a UUID)
    private String question;
}