package com.praj.datasetai.config;

import dev.langchain4j.memory.chat.ChatMemoryProvider;
import dev.langchain4j.memory.chat.MessageWindowChatMemory;
import dev.langchain4j.store.memory.chat.ChatMemoryStore;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AiConfig {

    @Bean
    public ChatMemoryProvider chatMemoryProvider(ChatMemoryStore chatMemoryStore) {
        // This tells LangChain4j: Whenever an @AiService needs memory,
        // use the PostgresChatMemoryStore to save/load it.
        return memoryId -> MessageWindowChatMemory.builder()
                .id(memoryId)
                .maxMessages(10) // Keeps the last 10 messages for context
                .chatMemoryStore(chatMemoryStore)
                .build();
    }
}