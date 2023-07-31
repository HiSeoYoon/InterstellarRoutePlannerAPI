package org.interstella.service;

import org.interstella.dto.JourneyRequest;
import org.interstella.dto.JourneyResponse;

public interface TransportService {
    JourneyResponse calculateCheapestTransport(JourneyRequest request);
}
