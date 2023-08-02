package org.interstella.dto;

import lombok.Data;
import org.interstella.model.TransportType;

@Data
public class JourneyResponse {
    private TransportType vehicle;
    private double cost;

    public JourneyResponse() {
    }
}
