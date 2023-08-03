package org.interstella.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.interstella.dto.AcceleratorDto;
import org.interstella.dto.RouteRequest;
import org.interstella.dto.RouteResponse;
import org.interstella.service.AcceleratorService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
@Api(tags = "Accelerator API", description = "Endpoints for managing accelerators")
public class AcceleratorController {
    private AcceleratorService acceleratorService;
    private final Logger logger = LoggerFactory.getLogger(AcceleratorController.class);

    public AcceleratorController(AcceleratorService acceleratorService) {
        this.acceleratorService = acceleratorService;
    }

    @ApiOperation("Get accelerator information list")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Successfully retrieved accelerator information list"),
            @ApiResponse(code = 404, message = "No accelerators found")
    })
    @GetMapping
    public ResponseEntity<List<AcceleratorDto>> getAllAccelerators() {
        List<AcceleratorDto> accelerators = acceleratorService.getAllAccelerators();
        logger.info("Getting all accelerators");
        if (accelerators.size() == 0) {
            logger.info("No accelerators found");
            return ResponseEntity.notFound().build();
        } else {
            logger.info("Returning {} accelerators", accelerators.size());
            return ResponseEntity.ok(accelerators);
        }
    }

    @ApiOperation("Get accelerator information by Id")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Successfully retrieved accelerator information"),
            @ApiResponse(code = 404, message = "No accelerator found with the given ID")
    })
    @GetMapping("/{acceleratorID}")
    public ResponseEntity<AcceleratorDto> getAccelerator(@PathVariable String acceleratorID) {
        logger.info("Getting accelerator with ID: {}", acceleratorID);
        AcceleratorDto accelerator = acceleratorService.getAcceleratorById(acceleratorID);

        if (accelerator != null) {
            logger.info("Accelerator found: {}, {}", accelerator.getId(), accelerator.getName());
            return ResponseEntity.ok(accelerator);
        } else {
            logger.info("No accelerator found with ID: {}", acceleratorID);
            return ResponseEntity.notFound().build();
        }
    }

    @ApiOperation("Get cheapest route from source accelerator to target accelerator")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Successfully retrieved cheapest route information"),
            @ApiResponse(code = 404, message = "No cheapest route found from the source to the target accelerator")
    })
    @GetMapping("/{acceleratorID}/to/{targetAcceleratorID}")
    public ResponseEntity<RouteResponse> getCheapestRoute(@PathVariable String acceleratorID, @PathVariable String targetAcceleratorID) {
        logger.info("Getting cheapest route from {} to {}", acceleratorID, targetAcceleratorID);
        RouteRequest routeRequest = new RouteRequest();
        routeRequest.setSource(acceleratorID);
        routeRequest.setDestination(targetAcceleratorID);

        RouteResponse routeResponse = acceleratorService.getCheapestRoute(routeRequest);

        if (routeResponse != null) {
            logger.info("Cheapest route found: {}, {}", routeResponse.getRoutes(), routeResponse.getJourneyFee());
            return ResponseEntity.ok(routeResponse);
        } else {
            logger.info("No cheapest route found from {} to {}", acceleratorID, targetAcceleratorID);
            return ResponseEntity.notFound().build();
        }
    }

    @ExceptionHandler(DataAccessException.class)
    public ResponseEntity<String> handleDataAccessException(DataAccessException ex) {
        logger.error("An error occurred while fetching data from the database", ex);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("An error occurred while fetching data from the database");
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<String> handleRuntimeException(RuntimeException ex) {
        logger.error("An unexpected error occurred", ex);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("An unexpected error occurred");
    }
}
