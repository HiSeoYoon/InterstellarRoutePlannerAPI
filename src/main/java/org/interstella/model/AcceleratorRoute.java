package org.interstella.model;

import lombok.Data;

import java.util.List;

@Data
public class AcceleratorRoute {
    private String sourceAcceleratorId;
    private String targetAcceleratorId;
    private List<String> journeyRoute;
    private double journeyFee;
}
