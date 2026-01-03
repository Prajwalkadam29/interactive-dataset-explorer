package com.praj.datasetai.repository;

import com.praj.datasetai.domain.ChatHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ChatHistoryRepository extends JpaRepository<ChatHistory, Long> {
    List<ChatHistory> findByMemoryIdOrderByCreatedAtAsc(String memoryId);
    void deleteByMemoryId(String memoryId);
}