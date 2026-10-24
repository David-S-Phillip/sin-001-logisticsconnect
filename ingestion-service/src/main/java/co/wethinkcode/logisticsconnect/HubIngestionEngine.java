package co.wethinkcode.logisticsconnect;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HubIngestionEngine {
    private final CsvFileReader reader = new CsvFileReader();
    private final HubMapper mapper = new HubMapper();

    public List<Hub> loadAndCleanHubs(String filePath) {
        List<String[]> rawRows = reader.readRawRows(filePath);
        Map<String, Hub> uniqueHubs = new HashMap<>();

        for (String[] row : rawRows) {
            Hub newHub = mapper.mapRowToHub(row);
            String key = newHub.getProvince() + "-" + newHub.getSortingCenter();

            if (uniqueHubs.containsKey(key)) {
                Hub existingHub = uniqueHubs.get(key);
                if (newHub.isActive() && !existingHub.isActive()) {
                    uniqueHubs.put(key, newHub);
                }
            } else {
                uniqueHubs.put(key, newHub);
            }
        }
        return new ArrayList<>(uniqueHubs.values());
    }
}