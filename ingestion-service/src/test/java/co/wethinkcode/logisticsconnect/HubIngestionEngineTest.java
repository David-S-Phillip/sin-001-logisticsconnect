package co.wethinkcode.logisticsconnect;
import org.junit.jupiter.api.Test;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

public class HubIngestionEngineTest {

    @Test
    public void testEngineDeduplicatesAndCleans(){
        HubIngestionEngine engine = new HubIngestionEngine();
        // Point to the controlled test file
        List<Hub> result = engine.loadAndCleanHubs("/test-engine.csv");

        // The file has 3 data rows, but 2 are duplicates for Gauteng-Johannesburg.
        // The list should only contain 2 items total.
        assertEquals(2, result.size());

        // Find Johannesburg and prove the active one survived
        Hub joburg = result.stream()
                .filter(h -> h.getProvince().equals("Gauteng"))
                .findFirst()
                .orElseThrow();

        assertEquals("H-101", joburg.getHubId());
        assertTrue(joburg.isActive());
    }
}
