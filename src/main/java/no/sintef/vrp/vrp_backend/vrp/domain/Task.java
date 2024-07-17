package no.sintef.vrp.vrp_backend.vrp.domain;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import org.optaplanner.core.api.domain.entity.PlanningEntity;
import org.optaplanner.core.api.domain.variable.PlanningVariable;
public class Task {
    private long id;
    private Location location;
    private Integer quantity;
    private boolean isPickup;

    public Task(){}

    public Task(long id, Location location, int quantity, boolean isPickup) {
        this.id = id;
        this.location = location;
        this.quantity = quantity;
        this.isPickup = isPickup;
    }

    public Integer getQuantity() {
        return quantity;
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

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public boolean isPickup() {
        return isPickup;
    }

    public void setPickup(boolean pickup) {
        isPickup = pickup;
    }
}
