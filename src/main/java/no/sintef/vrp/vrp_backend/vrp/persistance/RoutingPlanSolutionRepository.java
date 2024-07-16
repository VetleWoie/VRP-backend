package no.sintef.vrp.vrp_backend.vrp.persistance;

import no.sintef.vrp.vrp_backend.vrp.domain.RoutingPlan;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Repository
public class RoutingPlanSolutionRepository {

    private final Map<Long, RoutingPlan> solutionMap;

    public RoutingPlanSolutionRepository(){
        this.solutionMap = new HashMap<>();
    }

    public Optional<RoutingPlan> solution(Long problemId) {
        return Optional.ofNullable(solutionMap.get(problemId));
    }

    public void update(RoutingPlan routingPlan, Long problemId) {
        this.solutionMap.put(problemId, routingPlan);
    }
}
