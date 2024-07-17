package no.sintef.vrp.vrp_backend.vrp.domain;

import org.optaplanner.core.api.domain.solution.PlanningSolution;
import org.optaplanner.core.api.domain.solution.PlanningEntityCollectionProperty;
import org.optaplanner.core.api.domain.solution.ProblemFactCollectionProperty;
import org.optaplanner.core.api.domain.solution.PlanningScore;
import org.optaplanner.core.api.domain.valuerange.ValueRangeProvider;
import org.optaplanner.core.api.score.buildin.hardsoft.HardSoftScore;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@PlanningSolution
public class RoutingPlan {

    private Long id;

    private List<Vehicle> vehicleList;
    private List<Task> taskList;
    private List<PickupPoint> pickupPointList;
    private List<DropOffPoint> dropOffPointList;
    private HardSoftScore score;

    public RoutingPlan() {
        // No-arg constructor required by OptaPlanner
    }

    public RoutingPlan(Long id, List<Vehicle> vehicleList, List<Task> taskList, List<PickupPoint> pickupPointList, List<DropOffPoint> dropOffPointList) {
        this.id = id;
        this.vehicleList = vehicleList;
        this.taskList = taskList;
        this.pickupPointList = pickupPointList;
        this.dropOffPointList = dropOffPointList;
    }
// Getters and Setters

    @PlanningEntityCollectionProperty
    @ValueRangeProvider(id = "vehicleRange")
    public List<Vehicle> getVehicleList() {
        return vehicleList;
    }

    public void setVehicleList(List<Vehicle> vehicleList) {
        this.vehicleList = vehicleList;
    }

    @ProblemFactCollectionProperty
    public List<PickupPoint> getPickupPointList() {
        return pickupPointList;
    }

    public void setPickupPointList(List<PickupPoint> pickupPointList) {
        this.pickupPointList = pickupPointList;
    }

    @ProblemFactCollectionProperty
    public List<DropOffPoint> getDropOffPointList() {
        return dropOffPointList;
    }

    public void setDropOffPointList(List<DropOffPoint> dropoffPointList) {
        this.dropOffPointList = dropoffPointList;
    }

    @ProblemFactCollectionProperty
    @ValueRangeProvider(id = "taskRange")
    public List<Task> getTaskList() {
        return taskList;
    }

    public void setTaskList(List<Task> taskList) {
        this.taskList = taskList;
    }

    @ValueRangeProvider(id = "quantityRange")
    public List<Integer> getQuantityRange() {
        return IntStream.rangeClosed(0,20).boxed().collect(Collectors.toList());
    }

    @PlanningScore
    public HardSoftScore getScore() {
        return score;
    }

    public void setScore(HardSoftScore score) {
        this.score = score;
    }

    public Long getId() {
        return id;
    }
}
