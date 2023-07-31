package org.interstella.dto;

public class JourneyRequest {
    private double distance;
    private int passengers;
    private int parkingDays;

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public int getPassengers() {
        return passengers;
    }

    public void setPassengers(int passengers) {
        this.passengers = passengers;
    }

    public int getParkingDays() {
        return parkingDays;
    }

    public void setParkingDays(int parkingDays) {
        this.parkingDays = parkingDays;
    }
}
