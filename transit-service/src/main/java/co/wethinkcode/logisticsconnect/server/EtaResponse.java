package co.wethinkcode.logisticsconnect.server;

public class EtaResponse {
    private String hubId;
    private int etaDays;
    private int activeDelayStage;

    public EtaResponse(String hubId, int etaDays, int activeDelayStage) {
        this.hubId = hubId;
        this.etaDays = etaDays;
        this.activeDelayStage = activeDelayStage;
    }

    public EtaResponse() {}

    public String getHubId() { return hubId; }
    public int getEtaDays() { return etaDays; }
    public int getActiveDelayStage() { return activeDelayStage; }
}
