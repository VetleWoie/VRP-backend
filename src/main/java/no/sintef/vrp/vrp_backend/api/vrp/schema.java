package no.sintef.vrp.vrp_backend.api.vrp;

import java.util.List;

public class schema {
    public static class VehicleInput {
        long capacity;
        int idx;

        public long getCapacity() {
            return capacity;
        }

        public void setCapacity(long capacity) {
            this.capacity = capacity;
        }

        public int getIdx() {
            return idx;
        }

        public void setIdx(int idx) {
            this.idx = idx;
        }
    }
    public static class PickUpInput{
        public String getName() {
            return name;
        }

        public int getIdx() {
            return idx;
        }

        public void setIdx(int idx) {
            this.idx = idx;
        }

        public long getAmountAvailable() {
            return amountAvailable;
        }

        public void setAmountAvailable(long amountAvailable) {
            this.amountAvailable = amountAvailable;
        }

        public void setName(String name) {
            this.name = name;
        }

        String name;
        int idx;
        long amountAvailable;
    }
    public static class DropOffInput{
        String name;
        int idx;
        long amountNeeded;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getIdx() {
            return idx;
        }

        public void setIdx(int idx) {
            this.idx = idx;
        }

        public long getAmountNeeded() {
            return amountNeeded;
        }

        public void setAmountNeeded(long amountNeeded) {
            this.amountNeeded = amountNeeded;
        }
    }
    public static class LocationInput{
        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public List<Double> getDistances() {
            return distances;
        }

        public void setDistances(List<Double> distances) {
            this.distances = distances;
        }

        String name;
        List<Double> distances;
    }
    public static class NewProblem {
        @Override
        public String toString() {
            return "NewProblem{" +
                    "vehicleInputList=" + vehicleInputList +
                    ", pickUpInputList=" + pickUpInputList +
                    ", dropOffInputList=" + dropOffInputList +
                    ", locations=" + locations +
                    '}';
        }

        List<VehicleInput> vehicleInputList;
        List<PickUpInput> pickUpInputList;
        List<DropOffInput> dropOffInputList;
        List<LocationInput> locations;

        public List<VehicleInput> getVehicleInputList() {
            return vehicleInputList;
        }

        public void setVehicleInputList(List<VehicleInput> vehicleInputList) {
            this.vehicleInputList = vehicleInputList;
        }

        public List<PickUpInput> getPickUpInputList() {
            return pickUpInputList;
        }

        public void setPickUpInputList(List<PickUpInput> pickUpInputList) {
            this.pickUpInputList = pickUpInputList;
        }

        public List<DropOffInput> getDropOffInputList() {
            return dropOffInputList;
        }

        public void setDropOffInputList(List<DropOffInput> dropOffInputList) {
            this.dropOffInputList = dropOffInputList;
        }

        public List<LocationInput> getLocations() {
            return locations;
        }

        public void setLocations(List<LocationInput> locations) {
            this.locations = locations;
        }
    }
}
