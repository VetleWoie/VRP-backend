package no.sintef.vrp.vrp_backend.vrp.solver;

import no.sintef.vrp.vrp_backend.vrp.domain.*;
import org.optaplanner.core.api.score.buildin.hardsoft.HardSoftScore;
import org.optaplanner.core.api.score.stream.Constraint;
import org.optaplanner.core.api.score.stream.ConstraintFactory;
import org.optaplanner.core.api.score.stream.ConstraintProvider;
import org.optaplanner.core.api.score.stream.ConstraintCollectors;
import org.optaplanner.core.api.score.stream.Joiners;

public class RoutingConstraintProvider implements ConstraintProvider {


    @Override
    public Constraint[] defineConstraints(ConstraintFactory factory) {
        return new Constraint[]{
                vehicleCapacityConstraint(factory),
                pickupBeforeDropoffConstraint(factory),
                minimizeDistanceConstraint(factory),
                pickupPointAvailabilityConstraint(factory),
                dropoffPointDemandConstraint(factory)
        };
    }

    private Constraint vehicleCapacityConstraint(ConstraintFactory factory) {
        return factory.forEach(Vehicle.class)
                .penalize("Vehicle capacity exceeded", HardSoftScore.ONE_HARD, vehicle -> {
                    int pickedUp = 0;
                    int penalty = 0;
                    for (Task task : vehicle.getTasks()) {
                        if (task.isPickup()) {
                            pickedUp += task.getQuantity();
                            if (pickedUp > vehicle.getCapacity()) {
                                penalty += pickedUp - vehicle.getCapacity();
                            }
                        } else{
                            pickedUp -= task.getQuantity();
                        }
                    }
                    return penalty;
                });
    }

    private Constraint pickupBeforeDropoffConstraint(ConstraintFactory factory) {
        return factory.forEach(Vehicle.class)
                .filter(vehicle -> vehicle.getTasks() != null)
                .penalize("Pickup before dropoff", HardSoftScore.ONE_HARD, vehicle -> {
                    int pickedUp = 0;
                    int penalty = 0;
                    for (Task task : vehicle.getTasks()) {
                        if (task.isPickup()) {
                            pickedUp += task.getQuantity();

                        } else{
                            pickedUp -= task.getQuantity();
                            if (pickedUp < 0) {
                                penalty += Math.abs(pickedUp);
                            }
                        }
                    }
                    return penalty;
                });
    }

    private Constraint pickupPointAvailabilityConstraint(ConstraintFactory factory) {
        return factory.forEach(PickupPoint.class)
                .join(factory.forEach(Vehicle.class), Joiners.filtering((pickupPoint, vehicle) ->
                        vehicle.getTasks().stream()
                                .filter(task -> task.isPickup() && task.getLocation().equals(pickupPoint.getLocation()))
                                .mapToInt(Task::getQuantity).sum() > pickupPoint.getAmountAvailable()))
                .penalize("Pickup point availability exceeded", HardSoftScore.ONE_HARD, (pickupPoint, vehicle) -> {
                    int totalQuantity = vehicle.getTasks().stream()
                            .filter(task -> task.isPickup() && task.getLocation().equals(pickupPoint.getLocation()))
                            .mapToInt(Task::getQuantity).sum();
                    return totalQuantity - pickupPoint.getAmountAvailable();
                });
    }

    private Constraint dropoffPointDemandConstraint(ConstraintFactory factory) {
        return factory.forEach(DropOffPoint.class)
                .join(factory.forEach(Vehicle.class), Joiners.filtering((dropoffPoint, vehicle) ->
                        vehicle.getTasks().stream()
                                .filter(task -> !task.isPickup() && task.getLocation().equals(dropoffPoint.getLocation()))
                                .mapToInt(Task::getQuantity).sum() < dropoffPoint.getAmountNeeded()))
                .penalize("Dropoff point demand not met", HardSoftScore.ONE_HARD, (dropoffPoint, vehicle) -> {
                    int totalQuantity = vehicle.getTasks().stream()
                            .filter(task -> !task.isPickup() && task.getLocation().equals(dropoffPoint.getLocation()))
                            .mapToInt(Task::getQuantity).sum();
                    return dropoffPoint.getAmountNeeded() - totalQuantity;
                });
    }

    private Constraint minimizeDistanceConstraint(ConstraintFactory factory) {
        return factory.forEach(Vehicle.class)
                .penalize("Minimize distance", HardSoftScore.ONE_SOFT, vehicle -> {
                    int distance = 0;
                    Location previousLocation = vehicle.getStartLocation();
                    for (Task task : vehicle.getTasks()) {
                        distance += calculateDistance(previousLocation, task.getLocation());
                        previousLocation = task.getLocation();
                    }
                    return distance;
                });
    }

    private int calculateDistance(Location location1, Location location2) {
        if (location1 == null || location2 == null) {
            return 0;
        }
        return location1.getDistanceTo(location2);
    }
}

