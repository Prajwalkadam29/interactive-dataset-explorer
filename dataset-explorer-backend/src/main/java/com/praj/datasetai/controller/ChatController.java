package com.praj.datasetai.controller;

import com.praj.datasetai.dto.ChatRequest;
import com.praj.datasetai.dto.ChatResponse;
import com.praj.datasetai.langchain.agent.DataAnalystAgent;
import dev.langchain4j.store.memory.chat.ChatMemoryStore;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/chat")
@RequiredArgsConstructor
@CrossOrigin // This will allow our React app to call this API
public class ChatController {

    private final DataAnalystAgent agent;
    private final ChatMemoryStore chatMemoryStore; // Inject the Postgres store

    @PostMapping
    public ChatResponse ask(@RequestBody ChatRequest request) {
        // request.getSessionId() will be used by LangChain4j as the @MemoryId
        return agent.chat(request.getSessionId(), request.getQuestion());
    }

    /**
     * Endpoint to manually reset a conversation session.
     */
    @DeleteMapping("/session/{sessionId}")
    public ResponseEntity<String> resetSession(@PathVariable String sessionId) {
        // Talk directly to the store to wipe the history in Postgres
        chatMemoryStore.deleteMessages(sessionId);
        return ResponseEntity.ok("Session '" + sessionId + "' has been reset in PostgreSQL.");
    }
}