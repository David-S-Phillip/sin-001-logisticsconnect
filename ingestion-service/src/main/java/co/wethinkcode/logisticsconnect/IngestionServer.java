package co.wethinkcode.logisticsconnect;

import io.javalin.Javalin;
import java.util.Collections;
import java.util.List;

public class IngestionServer {

    private final Javalin app;
    private final List<Hub> cachedHubs;

    public IngestionServer() {
        // 1. Process the CSV exactly ONCE at startup
        HubIngestionEngine engine = new HubIngestionEngine();
        this.cachedHubs = Collections.unmodifiableList(engine.loadAndCleanHubs("/hubs-global.csv"));

        // 2. Setup Javalin
        this.app = Javalin.create();

        // 3. Define the routes
        this.app.get("/health", ctx -> ctx.result("OK"));

        // 4. Serve the pre-computed, clean list directly from RAM
        this.app.get("/hubs", ctx -> {
            ctx.json(cachedHubs);
            ctx.status(200);
        });
    }

    public void start(int port) {
        this.app.start(port);
    }

    public void stop() {
        this.app.stop();
    }
}