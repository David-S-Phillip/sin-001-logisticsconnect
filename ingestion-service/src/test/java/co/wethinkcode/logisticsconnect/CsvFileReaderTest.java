package co.wethinkcode.logisticsconnect;

import org.junit.jupiter.api.Test;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

public class CsvFileReaderTest {

    @Test
    public void testReadRawRowsSkipsHeaderAndEmptyLines() {
        CsvFileReader reader = new CsvFileReader();
        // Point to the test file in src/test/resources/
        List<String[]> rows = reader.readRawRows("/test-hubs.csv");

        // The file has 4 lines: 1 header, 2 data rows, 1 blank line.
        // It should only return the 2 data rows.
        assertEquals(2, rows.size());

        // Check the first data row
        String[] firstRow = rows.get(0);
        assertEquals("H-500", firstRow[0]);
        assertEquals("Gauteng", firstRow[1]);

        // Check the second data row
        String[] secondRow = rows.get(1);
        assertEquals("H-200", secondRow[0]);
    }

    @Test
    public void testFileNotFoundThrowsException() {
        CsvFileReader reader = new CsvFileReader();
        assertThrows(IllegalArgumentException.class, () -> {
            reader.readRawRows("/does-not-exist.csv");
        });
    }
}