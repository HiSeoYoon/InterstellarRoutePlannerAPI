package org.interstella.service;

import org.interstella.dto.JourneyRequest;
import org.interstella.dto.JourneyResponse;
import org.interstella.model.TransportType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TransportServiceTest {
    @InjectMocks
    private TransportServiceImpl transportServiceImpl;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testCalculateCheapestTransport_PersonalTransportCheaper() {
        JourneyRequest request = new JourneyRequest();
        request.setDistance(100.0);
        request.setPassengers(4);
        request.setParkingDays(2);

        JourneyResponse response = transportServiceImpl.calculateCheapestTransport(request);

        assertEquals(TransportType.PERSONAL_TRANSPORT, response.getVehicle());
        assertEquals(40.0, response.getCost());
    }

    @Test
    public void testCalculateCheapestTransport_HTCTransportCheaper() {
        JourneyRequest request = new JourneyRequest();
        request.setDistance(100.0);
        request.setPassengers(6);
        request.setParkingDays(10);

        JourneyResponse response = transportServiceImpl.calculateCheapestTransport(request);

        assertEquals(TransportType.HTC_TRANSPORT, response.getVehicle());
        assertEquals(90.0, response.getCost());
    }

}