package co.wethinkcode.logisticsconnect.server;

import io.javalin.Javalin;
import io.javalin.http.Context;
import java.util.HashMap;
import java.util.Map;

public class DelayStageServer {

    private final Javalin app;
    private final Map<String, Integer> stageCache;

    public DelayStageServer() {
        this.stageCache = new HashMap<>();
        this.app = Javalin.create();

        this.app.get("/health", ctx -> ctx.result("OK"));
        this.app.get("/delay-stage/{hubId}", this::getDelayStage);
        this.app.post("/delay-stage/{hubId}", this::updateDelayStage);
    }

    public void start(int port) {
        this.app.start(port);
    }

    public void stop() {
        this.app.stop();
    }

    private void getDelayStage(Context ctx) {
        String hubId = ctx.pathParam("hubId");
        // Default to stage 0 if no delay has been reported yet
        int stage = stageCache.getOrDefault(hubId, 0);

        DelayRecord record = new DelayRecord(hubId, stage);
        ctx.json(record);
        ctx.status(200);
    }

    private void updateDelayStage(Context ctx) {
        String hubId = ctx.pathParam("hubId");
        try {
            // Validate against the source of truth
            java.net.http.HttpClient client = java.net.http.HttpClient.newHttpClient();
            java.net.http.HttpRequest request = java.net.http.HttpRequest.newBuilder()
                    .uri(java.net.URI.create("http://localhost:7051/hub/" + hubId))
                    .GET()
                    .build();

            java.net.http.HttpResponse<String> response = client.send(request, java.net.http.HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 404) {
                ctx.status(400).result("Bad Request: Hub ID " + hubId + " does not exist.");
                return; // Stop execution, do not save to cache
            }

            // Hub is valid, proceed with Jackson mapping and cache update
            StageUpdate update = ctx.bodyAsClass(StageUpdate.class);
            stageCache.put(hubId, update.getStage());
            ctx.status(200).result("Updated " + hubId + " to stage " + update.getStage());

        } catch (Exception e) {
            ctx.status(500).result("Error validating hub: " + e.getMessage());
        }
    }

    // --- DTOs (Data Transfer Objects) for Jackson Serialization ---

    public static class DelayRecord {
        private String hubId;
        private int stage;

        public DelayRecord() {} // Empty constructor for Jackson

        public DelayRecord(String hubId, int stage) {
            this.hubId = hubId;
            this.stage = stage;
        }

        public String getHubId() { return hubId; }
        public int getStage() { return stage; }
    }

    public static class StageUpdate {
        private int stage;

        public StageUpdate() {} // Empty constructor for Jackson

        public int getStage() { return stage; }
    }
}