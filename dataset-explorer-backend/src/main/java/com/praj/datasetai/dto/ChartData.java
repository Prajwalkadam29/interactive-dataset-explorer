package com.praj.datasetai.dto;

import lombok.Data;
import java.util.List;
import java.util.Map;

@Data
public class ChartData {
    private String chartType;      // bar, line, pie, scatter, etc.
    private List<String> labels;   // X-axis labels
    private List<Double> values;   // Y-axis values

    private List<String> insights; // AI-generated bullets about the chart
}