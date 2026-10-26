package co.wethinkcode.logisticsconnect.client;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class TransitServiceClient {
    private final HttpClient httpClient;
    private final ObjectMapper mapper;

    public TransitServiceClient() {
        this.httpClient = HttpClient.newHttpClient();
        this.mapper = new ObjectMapper();
    }

    public JsonNode getHubDetails(String hubId) throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:7051/hub/" + hubId))
                .GET()
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() == 404) {
            return null; // Hub does not exist
        }
        return mapper.readTree(response.body());
    }

    public int getDelayStage(String hubId) throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:7052/delay-stage/" + hubId))
                .GET()
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        JsonNode node = mapper.readTree(response.body());
        return node.get("stage").asInt();
    }


}
