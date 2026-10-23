package co.wethinkcode.logisticsconnect;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class HubMapperTest {

    private final HubMapper mapper = new HubMapper();

    @Test
    public void testCleanStandardRow() {
        String[] raw = {" H-502 ", "gauteng", "Pretoria  North", "0"};
        Hub hub = mapper.mapRowToHub(raw);

        assertEquals("H-502", hub.getHubId());
        assertEquals("Gauteng", hub.getProvince()); // Title cased
        assertEquals("Pretoria North", hub.getSortingCenter()); // Double space fixed
        assertFalse(hub.isActive()); // "0" -> false
    }

    @Test
    public void testHandleMissingValues() {
        String[] raw = {"H-505", "N/A", "TBD", "unknown"};
        Hub hub = mapper.mapRowToHub(raw);

        assertEquals("H-505", hub.getHubId());
        assertNull(hub.getProvince());
        assertNull(hub.getSortingCenter());
        assertFalse(hub.isActive()); // Unrecognized boolean defaults to false
    }

    @Test
    public void testBooleanVariants() {
        assertTrue(mapper.mapRowToHub(new String[]{"ID", "Prov", "Center", "TRUE"}).isActive());
        assertTrue(mapper.mapRowToHub(new String[]{"ID", "Prov", "Center", "1"}).isActive());
        assertTrue(mapper.mapRowToHub(new String[]{"ID", "Prov", "Center", "yes"}).isActive());

        assertFalse(mapper.mapRowToHub(new String[]{"ID", "Prov", "Center", "FALSE"}).isActive());
        assertFalse(mapper.mapRowToHub(new String[]{"ID", "Prov", "Center", "0"}).isActive());
    }
}