package com.praj.datasetai.controller;

import com.praj.datasetai.dto.ChatRequest;
import com.praj.datasetai.dto.ChatResponse;
import com.praj.datasetai.langchain.agent.DataAnalystAgent;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/chat")
@RequiredArgsConstructor
@CrossOrigin // This will allow our React app to call this API
public class ChatController {

    private final DataAnalystAgent agent;

    @PostMapping
    public ChatResponse ask(@RequestBody ChatRequest request) {
        // request.getSessionId() will be used by LangChain4j as the @MemoryId
        return agent.chat(request.getSessionId(), request.getQuestion());
    }
}