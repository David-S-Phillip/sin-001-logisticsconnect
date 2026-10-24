package co.wethinkcode.logisticsconnect.server;

import io.javalin.Javalin;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class HubServiceServerTest {
    private static Javalin mockIngestionServer;
    private static HubServiceServer hubService;
    private static final int INGESTION_PORT = 7050;
    private static final int HUB_PORT = 7051;

    @BeforeAll
    public static void setup() {
        // 1. Spin up a fake ingestion server to intercept the startup fetch
        mockIngestionServer = Javalin.create().start(INGESTION_PORT);
        mockIngestionServer.get("/hubs", ctx -> {
            // Supply a hardcoded JSON string containing exactly one fake hub
            String fakeJson = "[{\"hubId\":\"H-999\",\"province\":\"Gauteng\",\"sortingCenter\":\"Test Center\",\"active\":true}]";
            ctx.json(fakeJson);
        });

        // 2. Boot the actual hub-service (it will unknowingly consume the fake JSON above)
        hubService = new HubServiceServer();
        hubService.start(HUB_PORT);

    }

    @AfterAll
    public static void teardown() {
        hubService.stop();
        mockIngestionServer.stop();
    }

    @Test
    public void testGetValidHubReturns200() throws Exception {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:" + HUB_PORT + "/hub/H-999"))
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());
        assertTrue(response.body().contains("Test Center"));
    }

    @Test
    public void testGetInvalidHubReturns404() throws Exception {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:" + HUB_PORT + "/hub/INVALID-ID"))
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(404, response.statusCode());
        assertEquals("Hub not found", response.body());
    }

}
