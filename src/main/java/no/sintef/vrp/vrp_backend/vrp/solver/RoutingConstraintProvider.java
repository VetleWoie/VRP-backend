package no.sintef.vrp.vrp_backend.vrp.solver;

import no.sintef.vrp.vrp_backend.vrp.domain.DropOffPoint;
import no.sintef.vrp.vrp_backend.vrp.domain.Location;
import no.sintef.vrp.vrp_backend.vrp.domain.PickupPoint;
import no.sintef.vrp.vrp_backend.vrp.domain.Task;
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
                pickupPointAvailabilityConstraint(factory),
                dropoffPointDemandConstraint(factory),
                minimizeDistanceConstraint(factory)
        };
    }

    private Constraint vehicleCapacityConstraint(ConstraintFactory factory) {
        return factory.forEach(Task.class)
                .filter(Task::isPickup)
                .groupBy(Task::getVehicle, ConstraintCollectors.sum(Task::getQuantity))
                .filter((vehicle, totalQuantity) -> totalQuantity > vehicle.getCapacity())
                .penalizeLong("Vehicle capacity exceeded", HardSoftScore.ONE_HARD, (vehicle, totalQuantity) -> totalQuantity - vehicle.getCapacity());
    }

    private Constraint pickupBeforeDropoffConstraint(ConstraintFactory factory) {
        return factory.forEach(Task.class)
                .filter(task -> !task.isPickup())
                .join(factory.forEach(Task.class),
                        Joiners.equal(Task::getVehicle),
                        Joiners.lessThan(Task::getId, Task::getId))
                .penalizeLong("Pickup before dropoff", HardSoftScore.ONE_HARD, (dropoffTask, pickupTask) -> 1L);
    }

    private Constraint pickupPointAvailabilityConstraint(ConstraintFactory factory) {
        return factory.forEach(PickupPoint.class)
                .join(factory.forEach(Task.class), Joiners.equal(PickupPoint::getLocation, Task::getLocation))
                .groupBy((pickupPoint, task) -> pickupPoint, ConstraintCollectors.sum((pickupPoint, task) -> task.getQuantity()))
                .filter((pickupPoint, totalQuantity) -> totalQuantity > pickupPoint.getAmountAvailable())
                .penalizeLong("Pickup point availability exceeded", HardSoftScore.ONE_HARD, (pickupPoint, totalQuantity) -> totalQuantity - pickupPoint.getAmountAvailable());
    }

    private Constraint dropoffPointDemandConstraint(ConstraintFactory factory) {
        return factory.forEach(DropOffPoint.class)
                .join(factory.forEach(Task.class), Joiners.equal(DropOffPoint::getLocation, Task::getLocation))
                .groupBy((dropoffPoint, task) -> dropoffPoint, ConstraintCollectors.sum((dropoffPoint, task) -> task.getQuantity()))
                .filter((dropoffPoint, totalQuantity) -> totalQuantity < dropoffPoint.getAmountNeeded())
                .penalizeLong("Dropoff point demand not met", HardSoftScore.ONE_HARD, (dropoffPoint, totalQuantity) -> dropoffPoint.getAmountNeeded() - totalQuantity);
    }

    private Constraint minimizeDistanceConstraint(ConstraintFactory factory) {
        return factory.forEach(Task.class)
                .join(factory.forEach(Task.class), Joiners.equal(Task::getVehicle))
                .penalizeLong("Minimize distance", HardSoftScore.ONE_SOFT, this::calculateDistance);
    }

    private Long calculateDistance(Task task1, Task task2) {
        if (task1 == null || task2 == null || task1.getVehicle() != task2.getVehicle()) {
            return 0L;
        }
        Location location1 = task1.getLocation();
        Location location2 = task2.getLocation();

        return location1.getDistanceTo(location2);
    }
}

