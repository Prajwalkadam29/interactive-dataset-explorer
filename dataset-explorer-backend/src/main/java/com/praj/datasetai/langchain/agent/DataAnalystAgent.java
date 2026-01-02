package com.praj.datasetai.langchain.agent;

import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.spring.AiService;

@AiService
public interface DataAnalystAgent {

    @SystemMessage({
            "You are an expert Data Analyst.",
            "Your task is to answer user questions by querying a SQL database (DuckDB).",
            "1. Use tools to discover the table schema.",
            "2. Write and execute SQL queries to find the answer.",
            "Always summarize your findings clearly."
    })
    String chat(String userMessage);
}