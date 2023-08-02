package org.interstella.service;

import org.interstella.dto.AcceleratorDto;
import org.interstella.dto.RouteRequest;
import org.interstella.dto.RouteResponse;
import org.interstella.model.Accelerator;
import org.interstella.model.AcceleratorConnection;
import org.interstella.repository.AcceleratorRepositoryImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.HashSet;
import java.util.HashMap;

@Service
public class AcceleratorServiceImpl implements AcceleratorService {
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    private AcceleratorRepositoryImpl acceleratorRepository;

    private static final double SPACEFLIGHT_COST_PER_PASSENGER_HU = 0.10;

    public AcceleratorServiceImpl(AcceleratorRepositoryImpl acceleratorRepository) {
        this.acceleratorRepository = acceleratorRepository;
    }

    @Override
    public List<AcceleratorDto> getAllAccelerators() {
        List<Accelerator> accelerators;

        try {
            log.info("Fetching all accelerators from the database");
            accelerators = acceleratorRepository.findAll();
        } catch (DataAccessException e) {
            log.error("Failed to fetch accelerators from the database", e);
            throw new RuntimeException("Failed to fetch accelerators from the database");
        }

        if (accelerators == null) {
            log.warn("No accelerators found in the database");
            throw new RuntimeException("No accelerators found in the database");
        }

        List<AcceleratorDto> acceleratorDtos = new ArrayList<>();

        for (Accelerator acc : accelerators) {
            AcceleratorDto dto = new AcceleratorDto();
            dto.setId(acc.getId());
            dto.setName(acc.getName());
            dto.setConnections(acc.getConnections());
            acceleratorDtos.add(dto);
        }

        log.info("Successfully fetched all accelerators");
        return acceleratorDtos;
    }

    @Override
    public AcceleratorDto getAcceleratorById(String acceleratorID) {
        Accelerator accelerator;

        try {
            log.info("Fetching accelerator by ID: {}", acceleratorID);
            accelerator = acceleratorRepository.findById(acceleratorID);
        } catch (DataAccessException e) {
            log.error("Failed to fetch accelerator from the database", e);
            throw new RuntimeException("Failed to fetch accelerators from the database");
        }

        if (accelerator == null) {
            log.warn("No accelerator found in the database with ID: {}", acceleratorID);
            throw new RuntimeException("No accelerators found in the database");
        }

        AcceleratorDto acceleratorDto = new AcceleratorDto();
        acceleratorDto.setId(accelerator.getId());
        acceleratorDto.setName(accelerator.getName());
        acceleratorDto.setConnections(accelerator.getConnections());

        log.info("Successfully fetched accelerator by ID: {}", acceleratorID);
        return acceleratorDto;
    }

    @Override
    public RouteResponse getCheapestRoute(RouteRequest routeRequest) {
        List<Accelerator> accelerators;

        try {
            log.info("Fetching all accelerators from the database");
            accelerators = acceleratorRepository.findAll();
        } catch (DataAccessException e) {
            log.error("Failed to fetch accelerators from the database", e);
            throw new RuntimeException("Failed to fetch accelerators from the database");
        }

        if (accelerators == null) {
            log.warn("No accelerators found in the database");
            throw new RuntimeException("No accelerators found in the database");
        }

        if (getAccelerator(accelerators, routeRequest.getSource()) == null ||
                getAccelerator(accelerators, routeRequest.getDestination()) == null) {
            log.warn("No accelerator not found by Id: {}, {}", routeRequest.getSource(), routeRequest.getDestination());
            return null;
        }

        List<String> cheapestRoute = dijkstra(accelerators, routeRequest.getSource(), routeRequest.getDestination());
        if (cheapestRoute == null) {
            log.warn("No routes between {}, {}", routeRequest.getSource(), routeRequest.getDestination());
            return null;
        }

        double cheapestJourneyFee = calculateJourneyFee(accelerators, cheapestRoute);

        RouteResponse routeResponse = new RouteResponse();
        routeResponse.setRoutes(cheapestRoute);
        routeResponse.setJourneyFee(cheapestJourneyFee);

        log.info("Successfully fetched all accelerators");
        return routeResponse;
    }

    private List<String> dijkstra(List<Accelerator> accelerators, String sourceId, String targetId) {
        Map<String, Double> distanceFromSource = new HashMap<>();
        Map<String, List<String>> paths = new HashMap<>();
        Set<String> visited = new HashSet<>();

        for (Accelerator accelerator : accelerators) {
            if (accelerator.getId().equals(sourceId)) {
                distanceFromSource.put(sourceId, 0.0);
            } else {
                distanceFromSource.put(accelerator.getId(), Double.MAX_VALUE);
            }
            paths.put(accelerator.getId(), new ArrayList<>());
        }

        while (true) {
            String currentID = null;
            double minDistance = Double.MAX_VALUE;

            for (String id : distanceFromSource.keySet()) {
                if (!visited.contains(id) && distanceFromSource.get(id) < minDistance) {
                    minDistance = distanceFromSource.get(id);
                    currentID = id;
                }
            }

            if (currentID == null) {
                break;
            }

            visited.add(currentID);
            Accelerator currentAccelerator = getAccelerator(accelerators, currentID);

            if (currentAccelerator == null) {
                continue;
            }

            for (AcceleratorConnection connection : currentAccelerator.getConnections()) {
                String neighborID = connection.getId();
                double distanceToNeighbor = connection.getDistance();

                double totalDistance = distanceFromSource.get(currentID) + distanceToNeighbor;

                if (totalDistance < distanceFromSource.get(neighborID)) {
                    distanceFromSource.put(neighborID, totalDistance);

                    List<String> pathToNeighbor = new ArrayList<>(paths.get(currentID));
                    pathToNeighbor.add(currentID);
                    paths.put(neighborID, pathToNeighbor);
                }
            }
        }

        List<String> pathsIncludingDest = paths.get(targetId);
        pathsIncludingDest.add(targetId);

        return pathsIncludingDest;
    }

    private double calculateJourneyFee(List<Accelerator> accelerators, List<String> route) {
        double journeyFee = 0.0;

        for (int i = 0; i < route.size() - 1; i++) {
            String sourceID = route.get(i);
            String targetID = route.get(i + 1);

            Accelerator accelerator = getAccelerator(accelerators, sourceID);
            if (accelerator == null) {
                continue;
            }

            AcceleratorConnection connection = getAcceleratorConnection(accelerator, targetID);
            if (connection == null) {
                continue;
            }

            double distanceToNeighbor = connection.getDistance();

            double spaceflightCost = SPACEFLIGHT_COST_PER_PASSENGER_HU * distanceToNeighbor;
            journeyFee += spaceflightCost;
        }

        return journeyFee;
    }

    private AcceleratorConnection getAcceleratorConnection(Accelerator accelerator, String targetID) {
        for (AcceleratorConnection connection : accelerator.getConnections()) {
            if (connection.getId().equals(targetID)) {
                return connection;
            }
        }
        return null;
    }

    private Accelerator getAccelerator(List<Accelerator> accelerators, String acceleratorID) {
        for (Accelerator accelerator : accelerators) {
            if (accelerator.getId().equals(acceleratorID)) {
                return accelerator;
            }
        }
        return null;
    }
}
