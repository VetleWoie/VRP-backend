package no.sintef.vrp.vrp_backend.vrp.domain;

import org.optaplanner.core.api.domain.entity.PlanningEntity;
import org.optaplanner.core.api.domain.variable.PlanningVariable;

@PlanningEntity
public class Task {
    private final long id;
    private Location location;
    private int quantity;
    private boolean isPickup;

    // Planning variable to assign a vehicle to a task
    @PlanningVariable(valueRangeProviderRefs = "vehicleRange")
    private Vehicle vehicle;

    // Planning variable to sequence tasks
    @PlanningVariable(valueRangeProviderRefs = "taskRange")
    private Task previousTask;

    public long getId() {
        return id;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public boolean isPickup() {
        return isPickup;
    }

    public void setPickup(boolean pickup) {
        isPickup = pickup;
    }

    public Vehicle getVehicle() {
        return vehicle;
    }

    public void setVehicle(Vehicle vehicle) {
        this.vehicle = vehicle;
    }

    public Task getPreviousTask() {
        return previousTask;
    }

    public void setPreviousTask(Task previousTask) {
        this.previousTask = previousTask;
    }

    public Task(long id, Location location, int quantity, boolean isPickup) {
        this.id = id;
        this.location = location;
        this.quantity = quantity;
        this.isPickup = isPickup;
    }
}
