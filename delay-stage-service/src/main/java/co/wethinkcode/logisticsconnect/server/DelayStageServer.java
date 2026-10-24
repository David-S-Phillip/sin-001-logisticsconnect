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

        // Javalin automatically uses Jackson to map the JSON body {"stage": 3} to this object
        StageUpdate update = ctx.bodyAsClass(StageUpdate.class);
        stageCache.put(hubId, update.getStage());

        ctx.status(200).result("Updated " + hubId + " to stage " + update.getStage());
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