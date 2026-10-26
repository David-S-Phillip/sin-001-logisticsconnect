package co.wethinkcode.logisticsconnect;

public class HubMapper {
    /**
     * This method is meant to read each line of the csv file, then map each item to a variable.
     * We run each variable through a "cleaner method" basically methods that ensure perfect strings can be used to create an instance of Hub.
     * @param columns
     * @return Hub(cleanID, cleanProvince, cleanCenter, cleanActive)
     */
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


    /**
     * method uses rawId and ensures that if any of the following is found - 'N/A, TBD, unknown, -, NaN' we replace that value with a null
     * @param input
     * @return String trimmed
     */
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

    /**
     *Takes rawActive and turns it into a boolean value, 'Y/N, yes/no, 1/0, true/FALSE'
     * @param input
     * @return String clean
     */
    private boolean parseBoolean(String input) {
        if (input == null) return false;
        String clean = input.trim().toLowerCase();
        // Handle the README's boolean variants
        return clean.equals("1") || clean.equals("true") || clean.equals("y") || clean.equals("yes");
    }

    /**
     * Takes rawProvinces and ensures each word 1st letter is uppercased while the rest are lowercase.
     * @param input
     * @return StringBuilder title
     */
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