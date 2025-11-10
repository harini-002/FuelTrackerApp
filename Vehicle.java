package model;

public class Vehicle {
    private int vehicleId;
    private String name;
    private String fuelType;

    public Vehicle(int vehicleId, String name, String fuelType) {
        this.vehicleId = vehicleId;
        this.name = name;
        this.fuelType = fuelType;
    }

    public int getVehicleId() { return vehicleId; }
    public String getName() { return name; }
    public String getFuelType() { return fuelType; }

    @Override
    public String toString() {
        return name + " (" + fuelType + ")";
    }
}
