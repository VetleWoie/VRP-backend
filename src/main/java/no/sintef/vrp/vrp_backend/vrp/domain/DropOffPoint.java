package no.sintef.vrp.vrp_backend.vrp.domain;

public class DropOffPoint {
    private long id;
    private Location location;
    private int amountNeeded;

    public long getId() {
        return id;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public int getAmountNeeded() {
        return amountNeeded;
    }

    public void setAmountNeeded(int amountNeeded) {
        this.amountNeeded = amountNeeded;
    }

    public DropOffPoint(long id, Location location, int amountNeeded) {
        this.id = id;
        this.location = location;
        this.amountNeeded = amountNeeded;
    }
    public DropOffPoint() {}
}
