package org.interstella.controller;

import org.interstella.dto.AcceleratorDto;
import org.interstella.dto.RouteRequest;
import org.interstella.dto.RouteResponse;
import org.interstella.service.AcceleratorService;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/accelerators")
public class AcceleratorController {
    private AcceleratorService acceleratorService;

    public AcceleratorController(AcceleratorService acceleratorService) {
        this.acceleratorService = acceleratorService;
    }

    @GetMapping
    public ResponseEntity<List<AcceleratorDto>> getAllAccelerators() {
        List<AcceleratorDto> accelerators = acceleratorService.getAllAccelerators();

        if (accelerators.size() == 0) {
            return ResponseEntity.notFound().build();
        } else {
            return ResponseEntity.ok(accelerators);
        }
    }

    @GetMapping("/{acceleratorID}")
    public ResponseEntity<AcceleratorDto> getAccelerator(@PathVariable String acceleratorID) {
        AcceleratorDto accelerator = acceleratorService.getAcceleratorById(acceleratorID);

        if (accelerator != null) {
            return ResponseEntity.ok(accelerator);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/{acceleratorID}/to/{targetAcceleratorID}")
    public ResponseEntity<RouteResponse> getCheapestRoute(@PathVariable String acceleratorID, @PathVariable String targetAcceleratorID) {
        RouteRequest routeRequest = new RouteRequest();
        routeRequest.setSource(acceleratorID);
        routeRequest.setDestination(targetAcceleratorID);

        RouteResponse accelerator = acceleratorService.getCheapestRoute(routeRequest);

        if (accelerator != null) {
            return ResponseEntity.ok(accelerator);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @ExceptionHandler(DataAccessException.class)
    public ResponseEntity<String> handleDataAccessException(DataAccessException ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("An error occurred while fetching data from the database");
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<String> handleRuntimeException(RuntimeException ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("An unexpected error occurred");
    }
}
