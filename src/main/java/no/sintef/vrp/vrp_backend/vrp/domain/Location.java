package no.sintef.vrp.vrp_backend.vrp.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.Map;

@JsonFormat(shape = JsonFormat.Shape.ARRAY)
@JsonIgnoreProperties({ "id" })
public class Location {

    private final long id;
    private Map<Long, Long> distanceMap;

    public Location(long id, Map<Long, Long> distanceMap) {
        this.id = id;
        this.distanceMap = distanceMap;
    }

    public long getId() {
        return id;
    }

    /**
     * Set the distance map. Distances are in meters.
     *
     * @param distanceMap a map containing distances from here to other locations
     */
    public void setDistanceMap(Map<Long, Long> distanceMap) {
        this.distanceMap = distanceMap;
    }

    /**
     * Distance to the given location in meters.
     *
     * @param location other location
     * @return distance in meters
     */
    public long getDistanceTo(Location location) {
        return distanceMap.get(location.id);
    }
}