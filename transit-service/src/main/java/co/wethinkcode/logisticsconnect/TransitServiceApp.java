package co.wethinkcode.logisticsconnect;

import co.wethinkcode.logisticsconnect.server.TransitServiceServer;
import io.javalin.Javalin;

public class TransitServiceApp {

    public static void main(String[] args) {
        TransitServiceServer server = new TransitServiceServer();

        server.start(7053);
        System.out.println("TransitServiceApp running on http://localhost:7053");

        // TODO (Calculates estimated arrival windows based on hub and delay stage.)
        // Add domain endpoints for transit-service here.
    }
}

// MQ TODO: subscribes to ActiveMQ topic MqConfig.TOPIC at MqConfig.BROKER_URL (see co.wethinkcode.logisticsconnect.mq.MqConfig)
