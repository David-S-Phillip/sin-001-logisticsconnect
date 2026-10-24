package co.wethinkcode.logisticsconnect.client;

import co.wethinkcode.logisticsconnect.Hub;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class HubServiceClient {

    private final HttpClient httpClient;
    private final ObjectMapper objectMapper;

    public HubServiceClient() {
        this.httpClient = HttpClient.newHttpClient();
        // Jackson's mapper does the heavy lifting for deserialization
        this.objectMapper = new ObjectMapper();
    }

    public Map<String, Hub> fetchAndCacheHubs(){
        try {
            // 1. Build the HTTP GET request targeting port 7050
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("http://localhost:7050/hubs"))
                    .GET()
                    .build();

            // 2. Execute the request and capture the raw JSON string
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() != 200) {
                throw new RuntimeException("Failed to fetch hubs. HTTP Status: " + response.statusCode());
            }

            // 3. Deserialize the JSON string directly into a List<Hub>
            List<Hub> hubList = objectMapper.readValue(
                    response.body(),
                    new TypeReference<List<Hub>>() {}
            );

            // 4. Transform the List into a HashMap using the hubId as the key
            return hubList.stream()
                    .collect(Collectors.toMap(Hub::getHubId, hub -> hub));

        } catch (Exception e) {
            throw new RuntimeException("Error communicating with Ingestion Service: " + e.getMessage(), e);
        }
    }
}
