package no.sintef.vrp.vrp_backend.vrp.persistance;

import no.sintef.vrp.vrp_backend.vrp.domain.RoutingPlan;
import no.sintef.vrp.vrp_backend.vrp.domain.VehicleRoutingSolution;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

// TODO: FIXME: REPLACE WITH REAL PERSISTANCE (I.E POSTGRES)
@Repository
public class RoutingPlanSolutionRepository {

    private final Map<Long, RoutingPlan> solutionMap;

    public RoutingPlanSolutionRepository(){
        this.solutionMap = new HashMap<>();
    }

    public Optional<RoutingPlan> solution(Long problemId) {
        return Optional.ofNullable(solutionMap.get(problemId));
    }

    public void update(RoutingPlan vehicleRoutingSolution, Long problemId) {
        this.solutionMap.put(problemId, vehicleRoutingSolution);
    }
}
