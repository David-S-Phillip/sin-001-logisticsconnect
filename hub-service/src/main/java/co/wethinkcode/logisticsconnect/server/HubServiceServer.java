package co.wethinkcode.logisticsconnect.server;
import co.wethinkcode.logisticsconnect.Hub;
import co.wethinkcode.logisticsconnect.client.HubServiceClient;
import io.javalin.Javalin;
import io.javalin.http.Context;
import java.util.Map;

public class HubServiceServer {
    private final Javalin app;
    private final Map<String, Hub> hubCache;

    public HubServiceServer() {
        // 1. Fetch and cache the data from Ingestion Service before the server even starts
        HubServiceClient client = new HubServiceClient();
        this.hubCache = client.fetchAndCacheHubs();

        this.app = Javalin.create();

        // 3. Define endpoints
        this.app.get("/health", ctx -> ctx.result("OK"));
        this.app.get("/hubs", this::getAllHubs);
        this.app.get("/hub/{id}", this::getHubById);

    }

    public void start(int port){
        this.app.start(port);
    }

    public void stop(){
        this.app.stop();
    }

    private void getAllHubs(Context ctx) {
        // Return all hubs by grabbing the values from the map
        ctx.json(hubCache.values());
        ctx.status(200);
    }

    private void getHubById(Context ctx) {
        // Extract the ID from the URL path
        String id = ctx.pathParam("id");

        // O(1) lookup using the HashMap
        Hub hub = hubCache.get(id);

        if (hub == null) {
            ctx.status(404).result("Hub not found");
        } else {
            ctx.json(hub);
            ctx.status(200);
        }
    }




}