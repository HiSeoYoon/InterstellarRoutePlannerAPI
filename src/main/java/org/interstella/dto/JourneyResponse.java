package org.interstella.dto;

import org.interstella.model.TransportType;

public class JourneyResponse {
    private TransportType vehicle;
    private double cost;

    public JourneyResponse() {
    }

    public TransportType getVehicle() {
        return vehicle;
    }

    public void setVehicle(TransportType vehicle) {
        this.vehicle = vehicle;
    }

    public double getCost() {
        return cost;
    }

    public void setCost(double cost) {
        this.cost = cost;
    }
}
