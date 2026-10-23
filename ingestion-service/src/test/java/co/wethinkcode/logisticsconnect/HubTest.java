package co.wethinkcode.logisticsconnect;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class HubTest {

    @Test
    public void testHubCreationAndGetters() {
        Hub hub = new Hub("H-001", "Gauteng", "Pretoria Central", true);

        assertEquals("H-001", hub.getHubId());
        assertEquals("Gauteng", hub.getProvince());
        assertEquals("Pretoria Central", hub.getSortingCenter());
        assertTrue(hub.isActive());
    }

    @Test
    public void testHubAllowsNullValues() {
        Hub hub = new Hub("H-002", null, null, false);

        assertEquals("H-002", hub.getHubId());
        assertNull(hub.getProvince());
        assertNull(hub.getSortingCenter());
        assertFalse(hub.isActive());
    }
}