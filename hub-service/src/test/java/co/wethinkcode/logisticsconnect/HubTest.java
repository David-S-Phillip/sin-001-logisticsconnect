package co.wethinkcode.logisticsconnect;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class HubTest {

    @Test
    public void testHubConstructorAndGetters() {
        Hub hub = new Hub("H-500", "Western Cape", "Cape Town", true);

        assertEquals("H-500", hub.getHubId());
        assertEquals("Western Cape", hub.getProvince());
        assertEquals("Cape Town", hub.getSortingCenter());
        assertTrue(hub.isActive());
    }

    @Test
    public void testEmptyConstructorForJackson() {
        Hub emptyHub = new Hub();
        assertNull(emptyHub.getHubId());
        assertFalse(emptyHub.isActive());
    }
}