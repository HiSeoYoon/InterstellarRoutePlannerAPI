package org.interstella.dto;

import java.util.List;

public class RouteResponse {
    private List<String> routes;
    private double journeyFee;

    public List<String> getRoutes() {
        return routes;
    }

    public void setRoutes(List<String> routes) {
        this.routes = routes;
    }

    public double getJourneyFee() {
        return journeyFee;
    }

    public void setJourneyFee(double journeyFee) {
        this.journeyFee = journeyFee;
    }
}
