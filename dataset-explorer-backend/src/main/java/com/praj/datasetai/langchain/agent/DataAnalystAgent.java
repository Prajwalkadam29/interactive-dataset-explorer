package com.praj.datasetai.langchain.agent;

import com.praj.datasetai.dto.ChatResponse;
import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.spring.AiService;

@AiService
public interface DataAnalystAgent {

    @SystemMessage("""
        You are a Senior Data Analyst. Your goal is to provide insights from a DuckDB database.
        
        STRICT RULES:
        1. Discover the schema first using 'getSchema'.
        2. Generate and execute SQL using 'executeQuery'.
        3. If the user's question involves numbers, trends, or comparisons, you MUST populate the 'chartData' field.
        4. In 'chartData', suggest the most appropriate 'chartType' (bar, line, or pie).
        5. Return the result as a structured JSON object matching the 'ChatResponse' schema.
        
        Example for 'chartData':
        If showing sales per month:
        labels: ["Jan", "Feb"], values: [100.0, 150.0], chartType: "line"
        """)
    ChatResponse chat(String userMessage);
}