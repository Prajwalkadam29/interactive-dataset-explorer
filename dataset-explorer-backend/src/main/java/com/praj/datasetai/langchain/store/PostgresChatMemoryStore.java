package com.praj.datasetai.langchain.store;

import com.praj.datasetai.domain.ChatHistory;
import com.praj.datasetai.repository.ChatHistoryRepository;
import org.springframework.context.annotation.Lazy;
import dev.langchain4j.data.message.ChatMessage;
import dev.langchain4j.data.message.ChatMessageDeserializer;
import dev.langchain4j.data.message.ChatMessageSerializer;
import dev.langchain4j.store.memory.chat.ChatMemoryStore;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class PostgresChatMemoryStore implements ChatMemoryStore {

    private final ChatHistoryRepository repository;

    // Use @Lazy to ensure JPA is ready before this store is created
    public PostgresChatMemoryStore(@Lazy ChatHistoryRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<ChatMessage> getMessages(Object memoryId) {
        return repository.findByMemoryIdOrderByCreatedAtAsc(memoryId.toString()).stream()
                .map(entity -> ChatMessageDeserializer.messageFromJson(entity.getMessageJson()))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void updateMessages(Object memoryId, List<ChatMessage> messages) {
        repository.deleteByMemoryId(memoryId.toString());

        List<ChatHistory> entities = messages.stream()
                .map(msg -> ChatHistory.builder()
                        .memoryId(memoryId.toString())
                        .messageJson(ChatMessageSerializer.messageToJson(msg))
                        .createdAt(LocalDateTime.now())
                        .build())
                .collect(Collectors.toList());

        repository.saveAll(entities);
    }

    @Override
    @Transactional
    public void deleteMessages(Object memoryId) {
        repository.deleteByMemoryId(memoryId.toString());
    }
}