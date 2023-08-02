package org.interstella.dto;

import lombok.Data;

import java.util.List;

@Data
public class RouteResponse {
    private List<String> routes;
    private double journeyFee;
}
