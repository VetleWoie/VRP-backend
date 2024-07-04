package no.sintef.vrp.vrp_backend.vrp.persistance;

import no.sintef.vrp.vrp_backend.vrp.domain.VehicleRoutingSolution;
import org.springframework.stereotype.Repository;

import java.util.Optional;

// TODO: FIXME: REPLACE WITH REAL PERSISTANCE (I.E POSTGRES)
@Repository
public class VehicleRoutingSolutionRepository {

    private VehicleRoutingSolution vehicleRoutingSolution;

    public Optional<VehicleRoutingSolution> solution() {
        return Optional.ofNullable(vehicleRoutingSolution);
    }

    public void update(VehicleRoutingSolution vehicleRoutingSolution) {
        this.vehicleRoutingSolution = vehicleRoutingSolution;
    }
}
