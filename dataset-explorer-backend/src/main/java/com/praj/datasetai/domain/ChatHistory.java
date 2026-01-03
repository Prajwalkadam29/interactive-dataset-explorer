package com.praj.datasetai.domain;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "chat_history")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class ChatHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String memoryId;

    @Column(columnDefinition = "TEXT")
    private String messageJson;

    private LocalDateTime createdAt;
}