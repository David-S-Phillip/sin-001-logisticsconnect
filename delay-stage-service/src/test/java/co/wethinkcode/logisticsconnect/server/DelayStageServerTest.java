package co.wethinkcode.logisticsconnect.server;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class DelayStageServerTest {
    private static DelayStageServer server;
    // Use a different port so it doesn't crash if your real server is currently running
    private static final int TEST_PORT = 7059;

    @BeforeAll
    public static void setup() {
        server = new DelayStageServer();
        server.start(TEST_PORT);
    }

    @AfterAll
    public static void teardown() {
        server.stop();
    }

    @Test
    public void testDefaultDelayStageIsZero() throws Exception {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:" + TEST_PORT + "/delay-stage/H-999"))
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());
        // Asserts the JSON body contains stage 0 for a previously unseen Hub ID
        assertTrue(response.body().contains("\"stage\":0"));
    }

    @Test
    public void testPostUpdatesDelayStage() throws Exception {
        HttpClient client = HttpClient.newHttpClient();

        // 1. Send the POST request to update the stage to 4
        HttpRequest postRequest = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:" + TEST_PORT + "/delay-stage/H-888"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString("{\"stage\": 4}"))
                .build();

        HttpResponse<String> postResponse = client.send(postRequest, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, postResponse.statusCode());

        // 2. Send a GET request to verify the stage is now 4 instead of 0
        HttpRequest getRequest = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:" + TEST_PORT + "/delay-stage/H-888"))
                .GET()
                .build();

        HttpResponse<String> getResponse = client.send(getRequest, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, getResponse.statusCode());
        // Asserts the JSON body successfully reflected the persisted POST data
        assertTrue(getResponse.body().contains("\"stage\":4"));
    }
}
