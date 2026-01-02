package com.praj.datasetai.dto;

import lombok.Data;
import java.util.List;

@Data
public class ChartData {
    private String chartType; // e.g., "bar", "line", "pie"
    private List<String> labels; // X-axis
    private List<Double> values; // Y-axis
}