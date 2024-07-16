package no.sintef.vrp.vrp_backend.vrp.domain;

import org.optaplanner.core.api.domain.entity.PlanningEntity;
import org.optaplanner.core.api.domain.variable.PlanningVariable;

@PlanningEntity
public class Task {
    private final long id;
    private Location location;
    private int quantity;
    private boolean isPickup;
    private Vehicle vehicle;
    private Task previousTask;



    // Planning variable to sequence tasks
    @PlanningVariable(valueRangeProviderRefs = "taskRange")
    public Task getPreviousTask() {
        return previousTask;
    }

    // Planning variable to assign a vehicle to a task
    @PlanningVariable(valueRangeProviderRefs = "vehicleRange")
    public Vehicle getVehicle() {
        return vehicle;
    }

    // Planning variable to determine amount to pick up or drop off
    @PlanningVariable(valueRangeProviderRefs = "quantityRange")
    public int getQuantity() {
        return quantity;
    }

    public Task(long id, Location location, int quantity, boolean isPickup) {
        this.id = id;
        this.location = location;
        this.quantity = quantity;
        this.isPickup = isPickup;
    }

    public long getId() {
        return id;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
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

    public void setVehicle(Vehicle vehicle) {
        this.vehicle = vehicle;
    }

    public void setPreviousTask(Task previousTask) {
        this.previousTask = previousTask;
    }
}
