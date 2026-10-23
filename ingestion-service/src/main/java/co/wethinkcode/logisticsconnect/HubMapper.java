package co.wethinkcode.logisticsconnect;

public class HubMapper {

    public Hub mapRowToHub(String[] columns) {
        if (columns.length < 4) {
            throw new IllegalArgumentException("Invalid row length");
        }

        String rawId = columns[0];
        String rawProvince = columns[1];
        String rawCenter = columns[2];
        String rawActive = columns[3];

        String cleanId = cleanString(rawId);
        String cleanProvince = titleCase(cleanString(rawProvince));
        String cleanCenter = cleanString(rawCenter).replaceAll("\\s{2,}", " "); // Fix double spaces
        boolean cleanActive = parseBoolean(rawActive);

        return new Hub(cleanId, cleanProvince, cleanCenter, cleanActive);
    }

    private String cleanString(String input) {
        if (input == null || input.trim().isEmpty()) return null;
        String trimmed = input.trim();
        // Handle the README's missing value variants
        if (trimmed.equalsIgnoreCase("N/A") ||
                trimmed.equalsIgnoreCase("TBD") ||
                trimmed.equalsIgnoreCase("unknown") ||
                trimmed.equals("-") ||
                trimmed.equalsIgnoreCase("NaN")) {
            return null;
        }
        return trimmed;
    }

    private boolean parseBoolean(String input) {
        if (input == null) return false;
        String clean = input.trim().toLowerCase();
        // Handle the README's boolean variants
        return clean.equals("1") || clean.equals("true") || clean.equals("y") || clean.equals("yes");
    }

    private String titleCase(String input) {
        if (input == null) return null;
        String[] words = input.toLowerCase().split(" ");
        StringBuilder title = new StringBuilder();
        for (String word : words) {
            if (!word.isEmpty()) {
                title.append(Character.toUpperCase(word.charAt(0)))
                        .append(word.substring(1))
                        .append(" ");
            }
        }
        return title.toString().trim();
    }
}