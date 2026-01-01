package com.praj.datasetai.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;

@Service
@Slf4j
@RequiredArgsConstructor
public class DataIngestionService {

    private final JdbcTemplate jdbcTemplate;

    public void ingestFile(String tableName, Path filePath) throws IOException {
        String extension = getFileExtension(filePath.toString());

        if ("csv".equalsIgnoreCase(extension)) {
            ingestCsv(tableName, filePath);
        } else if ("xlsx".equalsIgnoreCase(extension) || "xls".equalsIgnoreCase(extension)) {
            ingestExcel(tableName, filePath);
        } else {
            throw new IllegalArgumentException("Unsupported file format: " + extension);
        }
    }

    private void ingestCsv(String tableName, Path filePath) {
        String sql = String.format(
                "CREATE OR REPLACE TABLE %s AS SELECT * FROM read_csv_auto('%s')",
                tableName, filePath.toString().replace("\\", "/")
        );
        log.info("Ingesting CSV into table: {}", tableName);
        jdbcTemplate.execute(sql);
        log.info("Successfully ingested CSV into {}", tableName);
    }

    private void ingestExcel(String tableName, Path filePath) throws IOException {
        log.info("Converting Excel to CSV for ingestion: {}", tableName);
        Path tempCsv = filePath.getParent().resolve(tableName + "_temp.csv");

        try (Workbook workbook = new XSSFWorkbook(Files.newInputStream(filePath));
             PrintWriter writer = new PrintWriter(new FileOutputStream(tempCsv.toFile()))) {

            Sheet sheet = workbook.getSheetAt(0);
            for (Row row : sheet) {
                StringBuilder line = new StringBuilder();
                int lastColumn = Math.max(row.getLastCellNum(), 0);

                for (int i = 0; i < lastColumn; i++) {
                    Cell cell = row.getCell(i, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
                    String value = formatCellValue(cell);

                    // Escape quotes for CSV safety
                    line.append("\"").append(value.replace("\"", "\"\"")).append("\"");

                    if (i < lastColumn - 1) {
                        line.append(",");
                    }
                }
                writer.println(line);
            }
        }

        ingestCsv(tableName, tempCsv);
        Files.deleteIfExists(tempCsv);
    }

    private String formatCellValue(Cell cell) {
        if (cell == null) return "";

        return switch (cell.getCellType()) {
            case STRING -> cell.getStringCellValue();
            case NUMERIC -> {
                if (DateUtil.isCellDateFormatted(cell)) {
                    yield cell.getDateCellValue().toString();
                }
                yield String.valueOf(cell.getNumericCellValue());
            }
            case BOOLEAN -> String.valueOf(cell.getBooleanCellValue());
            case FORMULA -> {
                try {
                    yield cell.getStringCellValue();
                } catch (Exception e) {
                    yield String.valueOf(cell.getNumericCellValue());
                }
            }
            case BLANK -> "";
            default -> "";
        };
    }

    private String getFileExtension(String fileName) {
        if (fileName == null || !fileName.contains(".")) return "";
        return fileName.substring(fileName.lastIndexOf(".") + 1);
    }
}