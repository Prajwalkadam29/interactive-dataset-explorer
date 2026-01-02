package com.praj.datasetai.controller;

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
    public ChatResponse ask(@RequestBody String question) {
        // AI now returns a full object instead of just a string
        return agent.chat(question);
    }
}