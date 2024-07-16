package no.sintef.vrp.vrp_backend.vrp.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.Map;

@JsonFormat(shape = JsonFormat.Shape.ARRAY)
@JsonIgnoreProperties({ "id" })
public class Location {

    private final int id;
    private final String name;
    private final Long[] distanceMap;

    public Location(int id, String name, Long[] distanceMap) {
        this.id = id;
        this.name = name;
        this.distanceMap = distanceMap;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    /**
     * Distance to the given location in meters.
     *
     * @param location other location
     * @return distance in meters
     */
    public Long getDistanceTo(Location location) {
        return distanceMap[location.id];
    }
}