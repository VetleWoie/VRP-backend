package no.sintef.vrp.vrp_backend.api.vrp;

public class schema {
    public static class VehicleInput {
        long capacity;
        int idx;
    }
    public static class PickUpInput{
        String name;
        int idx;
        long amountAvailable;
    }
    public static class DropOffInput{
        String name;
        int idx;
        long amountNeeded;
    }
}
