package org.interstella.service;

import org.interstella.dto.JourneyRequest;
import org.interstella.dto.JourneyResponse;
import org.interstella.model.TransportType;
import org.springframework.stereotype.Service;

@Service
public class TransportServiceImpl implements TransportService {
    private static final double STANDARD_FUEL_COST = 0.30;
    private static final double PARKING_COST_PER_DAY = 5.0;
    private static final double HTC_TRANSPORT_COST = 0.45;
    private static final int PERSONAL_TRANSPORT_MAX_PASSENGER = 4;
    private static final int HTC_TRANSPORT_MAX_PASSENGER = 5;

    @Override
    public JourneyResponse calculateCheapestTransport(JourneyRequest request) {
        double personalTransportCost = calculatePersonalTransportCost(request);
        double htcTransportCost = calculateHTCTransportCost(request);

        TransportType cheapestVehicle;
        double cheapestCost;

        if (personalTransportCost < htcTransportCost) {
            cheapestVehicle = TransportType.PERSONAL_TRANSPORT;
            cheapestCost = personalTransportCost;
        } else {
            cheapestVehicle = TransportType.HTC_TRANSPORT;
            cheapestCost = htcTransportCost;
        }

        JourneyResponse response = new JourneyResponse();
        response.setVehicle(cheapestVehicle);
        response.setCost(cheapestCost);

        return response;
    }

    private double calculatePersonalTransportCost(JourneyRequest request) {
        int vehiclesNeeded = (int) Math.ceil((double) request.getPassengers() / PERSONAL_TRANSPORT_MAX_PASSENGER);
        double fuelCost = STANDARD_FUEL_COST * request.getDistance() * vehiclesNeeded;
        double parkingCost = PARKING_COST_PER_DAY * request.getParkingDays() * vehiclesNeeded;
        double totalCost = fuelCost + parkingCost;

        return totalCost;
    }

    private double calculateHTCTransportCost(JourneyRequest request) {
        int vehiclesNeeded = (int) Math.ceil((double) request.getPassengers() / HTC_TRANSPORT_MAX_PASSENGER);
        double totalCost = HTC_TRANSPORT_COST * request.getDistance() * vehiclesNeeded;

        return totalCost;
    }
}
