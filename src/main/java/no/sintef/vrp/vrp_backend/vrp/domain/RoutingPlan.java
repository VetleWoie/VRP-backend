package no.sintef.vrp.vrp_backend.vrp.domain;

import org.optaplanner.core.api.domain.solution.PlanningSolution;
import org.optaplanner.core.api.domain.solution.PlanningEntityCollectionProperty;
import org.optaplanner.core.api.domain.solution.ProblemFactCollectionProperty;
import org.optaplanner.core.api.domain.solution.PlanningScore;
import org.optaplanner.core.api.domain.valuerange.ValueRangeProvider;
import org.optaplanner.core.api.score.buildin.hardsoft.HardSoftScore;

import java.util.List;

@PlanningSolution
public class RoutingPlan {
    public void setVehicleList(List<Vehicle> vehicleList) {
        this.vehicleList = vehicleList;
    }

    public void setTaskList(List<Task> taskList) {
        this.taskList = taskList;
    }

    public void setPickupPointList(List<PickupPoint> pickupPointList) {
        this.pickupPointList = pickupPointList;
    }

    public List<DropOffPoint> getDropOffPointList() {
        return dropOffPointList;
    }

    public void setDropOffPointList(List<DropOffPoint> dropOffPointList) {
        this.dropOffPointList = dropOffPointList;
    }

    private long id;
    private List<Vehicle> vehicleList;
    private List<Task> taskList;
    private List<PickupPoint> pickupPointList;
    private List<DropOffPoint> dropOffPointList;

    @PlanningScore
    private HardSoftScore score;

    // Getters and Setters

    @ProblemFactCollectionProperty
    @ValueRangeProvider(id = "vehicleRange")
    public List<Vehicle> getVehicleList() {
        return vehicleList;
    }

    @ProblemFactCollectionProperty
    public List<PickupPoint> getPickupPointList() {
        return pickupPointList;
    }

    @ProblemFactCollectionProperty
    public List<DropOffPoint> getDropoffPointList() {
        return dropOffPointList;
    }

    @PlanningEntityCollectionProperty
    @ValueRangeProvider(id = "taskRange")
    public List<Task> getTaskList() {
        return taskList;
    }

    public HardSoftScore getScore() {
        return score;
    }

    public void setScore(HardSoftScore score) {
        this.score = score;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
    // other necessary methods
}
