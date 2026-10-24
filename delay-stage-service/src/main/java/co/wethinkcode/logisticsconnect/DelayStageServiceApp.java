package co.wethinkcode.logisticsconnect;

import co.wethinkcode.logisticsconnect.server.DelayStageServer;
import io.javalin.Javalin;

public class DelayStageServiceApp {

    public static void main(String[] args) {
        DelayStageServer server = new DelayStageServer();
        // Start on port 7052 per the integration contract
        server.start(7052);
        System.out.println("DelayStageServiceApp running on http://localhost:7052");

        // TODO (Tracks the Transit Delay Stage (0-8, e.g. weather shutdowns).)
        // Add domain endpoints for delay-stage-service here.
    }
}

// MQ TODO: publishes to ActiveMQ topic MqConfig.TOPIC at MqConfig.BROKER_URL (see co.wethinkcode.logisticsconnect.mq.MqConfig)
