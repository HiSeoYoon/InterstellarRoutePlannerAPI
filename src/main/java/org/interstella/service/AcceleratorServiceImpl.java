package org.interstella.service;

import org.interstella.dto.RouteRequest;
import org.interstella.dto.RouteResponse;
import org.interstella.model.Accelerator;
import org.interstella.model.AcceleratorConnection;
import org.interstella.repository.AcceleratorRepositoryImpl;
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
    private AcceleratorRepositoryImpl acceleratorRepository;

    private static final double SPACEFLIGHT_COST_PER_PASSENGER_HU = 0.10;

    public AcceleratorServiceImpl(AcceleratorRepositoryImpl acceleratorRepository) {
        this.acceleratorRepository = acceleratorRepository;
    }

    @Override
    public List<Accelerator> getAllAccelerators() {
        return acceleratorRepository.findAll();
    }

    @Override
    public Accelerator getAcceleratorById(String acceleratorID) {
        return acceleratorRepository.findById(acceleratorID);
    }

    @Override
    public RouteResponse getCheapestRoute(RouteRequest routeRequest) {
        List<Accelerator> accelerators;

        try {
            accelerators = acceleratorRepository.findAll();
        } catch (DataAccessException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to fetch accelerators from the database");
        }

        if (accelerators == null) {
            throw new RuntimeException("No accelerators found in the database");
        }

        if (getAccelerator(accelerators, routeRequest.getSource()) == null ||
                getAccelerator(accelerators, routeRequest.getDestination()) == null) {
            return null;
        }

        List<String> cheapestRoute = dijkstra(accelerators, routeRequest.getSource(), routeRequest.getDestination());
        if (cheapestRoute == null) {
            return null;
        }

        double cheapestJourneyFee = calculateJourneyFee(accelerators, cheapestRoute);

        RouteResponse routeResponse = new RouteResponse();
        routeResponse.setRoutes(cheapestRoute);
        routeResponse.setJourneyFee(cheapestJourneyFee);

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
