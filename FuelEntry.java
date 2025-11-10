package model;

import java.time.LocalDate;

public class FuelEntry {
    private int entryId;
    private int vehicleId;
    private LocalDate date;
    private double liters;
    private double cost;
    private double odometer;

    public FuelEntry(int entryId, int vehicleId, LocalDate date, double liters, double cost, double odometer) {
        this.entryId = entryId;
        this.vehicleId = vehicleId;
        this.date = date;
        this.liters = liters;
        this.cost = cost;
        this.odometer = odometer;
    }

    public int getEntryId() { return entryId; }
    public int getVehicleId() { return vehicleId; }
    public LocalDate getDate() { return date; }
    public double getLiters() { return liters; }
    public double getCost() { return cost; }
    public double getOdometer() { return odometer; }

    @Override
    public String toString() {
        return date + " | " + liters + "L | ₹" + cost + " | " + odometer + " km";
    }
}
