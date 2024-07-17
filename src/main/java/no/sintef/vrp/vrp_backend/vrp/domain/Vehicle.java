package no.sintef.vrp.vrp_backend.vrp.domain;

import org.optaplanner.core.api.domain.entity.PlanningEntity;
import org.optaplanner.core.api.domain.lookup.PlanningId;
import org.optaplanner.core.api.domain.variable.PlanningListVariable;

import java.util.ArrayList;
import java.util.List;

@PlanningEntity
public class Vehicle {
    @PlanningId
    private long id;
    private Location startLocation;
    private int capacity;

    @PlanningListVariable(valueRangeProviderRefs = "taskRange")
    private List<Task> tasks = new ArrayList<Task>();

    public Vehicle(){}
    public Vehicle(long id, Location startLocation, int capacity) {
        this.id = id;
        this.startLocation = startLocation;
        this.capacity = capacity;
    }

    public List<Task> getTasks() {
        return tasks;
    }

    public void setTasks(List<Task> tasks) {
        this.tasks = tasks;
    }


    public long getId() {
        return id;
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

    @Override
    public String toString() {
        return "Vehicle{" +
                "id=" + id +
                ", startLocation=" + startLocation +
                ", capacity=" + capacity +
                '}';
    }
}