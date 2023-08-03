package org.interstella.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.interstella.dto.JourneyRequest;
import org.interstella.dto.JourneyResponse;
import org.interstella.service.TransportService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
@Api(tags = "Transport API", description = "Endpoints for calculate journey")
public class TransportController {
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    private TransportService transportService;

    public TransportController(TransportService transportService) {
        this.transportService = transportService;
    }

    @GetMapping("/transport/{distance}")
    @ApiOperation(value = "Calculate the cheapest journey",
            notes = "Calculate the cheapest journey based on the given distance, passengers, and parking.")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Successfully calculated the cheapest journey"),
            @ApiResponse(code = 400, message = "Invalid input: Distance, passengers, and parking must be greater than 0.")
    })
    public ResponseEntity<JourneyResponse> calculateCheapestJourney(
            @PathVariable double distance,
            @RequestParam(required = true) int passengers,
            @RequestParam(required = true) int parking) {

        if (distance <= 0 || passengers <= 0 || parking <= 0) {
            log.error("Invalid input: Distance={}, Passengers={}, Parking={}", distance, passengers, parking);
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Distance, passengers, and parking must be greater than 0.");
        }

        log.info("Received request: Distance={}, Passengers={}, Parking={}", distance, passengers, parking);

        JourneyRequest request = new JourneyRequest();
        request.setDistance(distance);
        request.setPassengers(passengers);
        request.setParkingDays(parking);

        JourneyResponse response = transportService.calculateCheapestTransport(request);

        log.info("Calculated cheapest journey response: Vehicle={}, Cost={}", response.getVehicle(), response.getCost());

        return ResponseEntity.ok(response);
    }
}
