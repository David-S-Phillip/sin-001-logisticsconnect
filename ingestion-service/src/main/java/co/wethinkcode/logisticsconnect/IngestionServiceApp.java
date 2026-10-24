package co.wethinkcode.logisticsconnect;

import io.javalin.Javalin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class IngestionServiceApp {

    public static void main(String[] args) {
        IngestionServer server = new IngestionServer();

        server.start(7050);

        System.out.println("IngestionServiceApp running on http://localhost:7050");

        // TODO: read and clean src/main/resources/hubs-global.csv (hubs, sorting centers, regional districts data —
        // trim whitespace, fix casing, normalize dates/booleans) and expose the
        // cleaned records here for the other services to consume.

    }

}

