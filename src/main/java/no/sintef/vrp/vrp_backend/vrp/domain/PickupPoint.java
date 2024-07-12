package no.sintef.vrp.vrp_backend.vrp.domain;

public class PickupPoint {
    private final long id;
    private Location location;
    private int amountAvailable;

    public long getId() {
        return id;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public int getAmountAvailable() {
        return amountAvailable;
    }

    public void setAmountAvailable(int amountAvailable) {
        this.amountAvailable = amountAvailable;
    }

    public PickupPoint(long id, Location location, int amountAvailable) {
        this.id = id;
        this.location = location;
        this.amountAvailable = amountAvailable;
    }
}
