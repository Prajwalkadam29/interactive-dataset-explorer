package com.praj.datasetai.dto;

import lombok.Data;
import java.util.List;
import java.util.Map;

@Data
public class ChartData {
    private String chartType;      // bar, line, pie, scatter, etc.
    private List<String> labels;   // X-axis labels
    private List<Double> values;   // Y-axis values

    // For Scatter plots or Heatmaps, we can use a list of coordinate maps
    // Example: [{"x": 10, "y": 20}, {"x": 15, "y": 25}]
    private List<Map<String, Object>> multiSeriesData;

    private List<String> insights; // AI-generated bullets about the chart
}