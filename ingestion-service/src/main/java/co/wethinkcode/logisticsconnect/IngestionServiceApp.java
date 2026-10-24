package co.wethinkcode.logisticsconnect;

import io.javalin.Javalin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class IngestionServiceApp {

    public static void main(String[] args) {
        CsvFileReader reader = new CsvFileReader();
        HubMapper mapper = new HubMapper();

        Javalin app = Javalin.create().start(7050);

        app.get("/health", ctx -> ctx.result("OK"));

        // TODO: read and clean src/main/resources/hubs-global.csv (hubs, sorting centers, regional districts data —
        // trim whitespace, fix casing, normalize dates/booleans) and expose the
        // cleaned records here for the other services to consume.

        app.get("/hubs", ctx -> {
            try {
                // Get raw rows from your reader
                List<String[]> rawRows = reader.readRawRows("/hubs-global.csv");

                // Map for deduplication
                Map<String, Hub> uniqueHubs = new HashMap<>();

                // Loop through, map, and deduplicate
                for (String[] row : rawRows) {
                    Hub newHub = mapper.mapRowToHub(row);

                    // The unique key to catch duplicates
                    String key = newHub.getProvince() + "-" + newHub.getSortingCenter();

                    if (uniqueHubs.containsKey(key)) {
                        Hub existingHub = uniqueHubs.get(key);
                        // Business Rule: Active hubs overwrite inactive hubs
                        if (newHub.isActive() && !existingHub.isActive()) {
                            uniqueHubs.put(key, newHub);
                        }
                    } else {
                        uniqueHubs.put(key, newHub);
                    }
                }

                // Extract the final, clean list of Hubs
                List<Hub> cleanHubs = new ArrayList<>(uniqueHubs.values());

                // Javalin automatically serializes this List into a JSON array
                ctx.json(cleanHubs);
                ctx.status(200);

            } catch (Exception e) {
                // If anything fails (like a missing file), return a 500 error
                ctx.status(500).result("Failed to process hubs: " + e.getMessage());
            }
        });

        System.out.println("IngestionServiceApp running on http://localhost:7050");
    }
    }

