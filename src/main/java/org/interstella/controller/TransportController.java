package org.interstella.controller;

import org.interstella.dto.JourneyRequest;
import org.interstella.dto.JourneyResponse;
import org.interstella.service.TransportService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
public class TransportController {
    private TransportService transportService;

    public TransportController(TransportService transportService) {
        this.transportService = transportService;
    }

    @GetMapping("/transport/{distance}")
    public ResponseEntity<JourneyResponse> calculateCheapestJourney(
            @PathVariable double distance,
            @RequestParam(required = true) int passengers,
            @RequestParam(required = true) int parking) {

        if (distance <= 0 || passengers <= 0 || parking <= 0) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Distance, passengers, and parking must be greater than 0.");
        }

        JourneyRequest request = new JourneyRequest();
        request.setDistance(distance);
        request.setPassengers(passengers);
        request.setParkingDays(parking);

        JourneyResponse response = transportService.calculateCheapestTransport(request);
        return ResponseEntity.ok(response);
    }
}
