package co.wethinkcode.logisticsconnect;

/**
 * This class is a real world representation of a hub.
 * A hub is warehouse where packages arrive, get sorted and are sent to their next destination.
 */
public class Hub {
    private final String hubId;
    private final String province;
    private final String sortingCenter;
    private final boolean active;

    public Hub(String hubId, String province, String sortingCenter, boolean active) {
        this.hubId = hubId;
        this.province = province;
        this.sortingCenter = sortingCenter;
        this.active = active;
    }

    // Add getters for all fields
    public String getHubId() { return hubId; }
    public String getProvince() { return province; }
    public String getSortingCenter() { return sortingCenter; }
    public boolean isActive() { return active; }
}