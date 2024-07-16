package no.sintef.vrp.vrp_backend.vrp.domain;

import org.optaplanner.core.api.domain.lookup.PlanningId;

public class Vehicle {
    @PlanningId
    private final long id;
    private Location startLocation;
    private int capacity;

    public Vehicle(long id, Location startLocation, int capacity) {
        this.id = id;
        this.startLocation = startLocation;
        this.capacity = capacity;
    }

    public long getId() {
        return id;
    }

    @Override
    public String toString() {
        return "Vehicle{" +
                "id=" + id +
                ", startLocation=" + startLocation +
                ", capacity=" + capacity +
                '}';
    }

    public Location getStartLocation() {
        return startLocation;
    }

    public void setStartLocation(Location startLocation) {
        this.startLocation = startLocation;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }
}