package co.wethinkcode.logisticsconnect.client;

import co.wethinkcode.logisticsconnect.Hub;
import io.javalin.Javalin;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Map;
import static org.junit.jupiter.api.Assertions.*;

public class HubServiceClientTest {

    private static Javalin mockIngestionServer;

    @BeforeAll
    public static void setup() {
        mockIngestionServer = Javalin.create().start(7050);
        mockIngestionServer.get("/hubs", ctx -> {
            String fakeJson = "[{\"hubId\":\"H-777\",\"province\":\"Gauteng\",\"sortingCenter\":\"Midrand\",\"active\":true}]";
            ctx.json(fakeJson);
        });
    }

    @AfterAll
    public static void teardown() {
        mockIngestionServer.stop();
    }

    @Test
    public void testFetchAndCacheHubs() {
        HubServiceClient client = new HubServiceClient();
        Map<String, Hub> result = client.fetchAndCacheHubs();

        assertEquals(1, result.size());
        assertTrue(result.containsKey("H-777"));

        Hub hub = result.get("H-777");
        assertEquals("Midrand", hub.getSortingCenter());
        assertTrue(hub.isActive());
    }




}
