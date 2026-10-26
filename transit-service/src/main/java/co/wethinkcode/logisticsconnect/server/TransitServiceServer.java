package co.wethinkcode.logisticsconnect.server;

import co.wethinkcode.logisticsconnect.client.TransitServiceClient;
import com.fasterxml.jackson.databind.JsonNode;
import io.javalin.Javalin;
import io.javalin.http.Context;
public class TransitServiceServer {
    private final Javalin app;
    private final TransitServiceClient client;

    public TransitServiceServer() {
        this.app = Javalin.create();
        this.client = new TransitServiceClient();

        this.app.get("/health", ctx -> ctx.result("OK"));
        this.app.get("/eta/{hubId}", this::calculateEta);
    }

    public void start(int port) {
        this.app.start(port);
    }

    public void stop() {
        this.app.stop();
    }

    private void calculateEta(Context ctx) {
        String hubId = ctx.pathParam("hubId");

        try {
            // 1. Ask hub-service if the destination exists
            JsonNode hubDetails = client.getHubDetails(hubId);
            if (hubDetails == null) {
                ctx.status(404).result("Routing rejected: Destination Hub not found");
                return;
            }

            // 2. Ask delay-stage-service for the current penalty
            int delayStage = client.getDelayStage(hubId);

            // 3. Apply your business logic
            int baseDays = 2;
            int etaDays = baseDays + delayStage;

            // 4. Return the calculated timeline
            ctx.json(new EtaResponse(hubId, etaDays, delayStage));
            ctx.status(200);

        } catch (Exception e) {
            ctx.status(500).result("Internal routing error: " + e.getMessage());
        }
    }


}
