package co.wethinkcode.logisticsconnect;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/**
 * This class takes in the .csv file inside the resources folder
 * Use one method called readRawRows()
 * it returns a List of array strings --> similar to a table format
 */
public class CsvFileReader {

    public List<String[]> readRawRows(String resourcePath) {
        List<String[]> rawRows = new ArrayList<>();

        // 1. Fetch file stream from resources folder
        InputStream inputStream = CsvFileReader.class.getResourceAsStream(resourcePath);

        if (inputStream == null) {
            throw new IllegalArgumentException("Resource file not found: " + resourcePath);
        }

        // 2. Wrap stream in a BufferedReader for efficient line-by-line reading
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {
            String line;
            boolean isHeader = true;

            while ((line = reader.readLine()) != null) {
                // Skip header line (hub_id, Province ,sorting_center,active)
                if (isHeader) {
                    isHeader = false;
                    continue;
                }

                // Skip blank/empty lines
                if (line.trim().isEmpty()) {
                    continue;
                }

                // Split line by comma (-1 preserves empty trailing fields)
                String[] columns = line.split(",", -1);
                rawRows.add(columns);
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to read CSV file: " + resourcePath, e);
        }

        return rawRows;
    }
}