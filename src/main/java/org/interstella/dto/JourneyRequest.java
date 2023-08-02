package org.interstella.dto;

import lombok.Data;

@Data
public class JourneyRequest {
    private double distance;
    private int passengers;
    private int parkingDays;
}
