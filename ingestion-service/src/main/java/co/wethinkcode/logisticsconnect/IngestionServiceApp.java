package co.wethinkcode.logisticsconnect;

import io.javalin.Javalin;

import java.util.List;

public class IngestionServiceApp {

    public static void main(String[] args) {
        CsvFileReader reader = new CsvFileReader();
        List<String[]> rawRows = reader.readRawRows("/hubs-global.csv");
        // 3. Print count and preview the first 5 raw rows
        System.out.println("Total raw CSV rows loaded (excluding header): " + rawRows.size());
        System.out.println("--- First 5 Raw Rows ---");

        Javalin app = Javalin.create().start(7050);

        app.get("/health", ctx -> ctx.result("OK"));

        // TODO: read and clean src/main/resources/hubs-global.csv (hubs, sorting centers, regional districts data —
        // trim whitespace, fix casing, normalize dates/booleans) and expose the
        // cleaned records here for the other services to consume.
    }
}
