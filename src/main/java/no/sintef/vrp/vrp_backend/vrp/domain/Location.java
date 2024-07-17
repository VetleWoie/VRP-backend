package no.sintef.vrp.vrp_backend.vrp.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonFormat(shape = JsonFormat.Shape.ARRAY)
@JsonIgnoreProperties({ "id" })
public class Location {

    private int id;
    private String name;
    private Integer[] distanceMap;

    public Location(){}

    public Location(int id, String name, Integer[] distanceMap) {
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
    public int getDistanceTo(Location location) {
        return distanceMap[location.id];
    }
}