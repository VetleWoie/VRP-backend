package no.sintef.vrp.vrp_backend.vrp.persistance;

import no.sintef.vrp.vrp_backend.vrp.domain.VehicleRoutingSolution;
import org.springframework.stereotype.Repository;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

// TODO: FIXME: REPLACE WITH REAL PERSISTANCE (I.E POSTGRES)
@Repository
public class VehicleRoutingSolutionRepository {

    private Map<Long, VehicleRoutingSolution> solutionMap;

    public VehicleRoutingSolutionRepository(){
        this.solutionMap = new HashMap<>();
    }

    public Optional<VehicleRoutingSolution> solution(Long problemId) {
        return Optional.ofNullable(solutionMap.get(problemId));
    }

    public void update(VehicleRoutingSolution vehicleRoutingSolution, Long problemId) {
        this.solutionMap.put(problemId, vehicleRoutingSolution);
    }
}
