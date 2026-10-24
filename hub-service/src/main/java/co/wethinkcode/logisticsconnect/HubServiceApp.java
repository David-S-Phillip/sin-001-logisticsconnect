package co.wethinkcode.logisticsconnect;

import co.wethinkcode.logisticsconnect.server.HubServiceServer;
import io.javalin.Javalin;

public class HubServiceApp {

    public static void main(String[] args) {
        HubServiceServer server = new HubServiceServer();

        server.start(7051);
        System.out.println("HubServiceApp running on http://localhost:7051");
        // TODO (Serves provinces and sorting centers (place-name source of truth).)
        // Add domain endpoints for hub-service here.
    }
}
