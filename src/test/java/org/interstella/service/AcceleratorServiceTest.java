package org.interstella.service;

import org.interstella.dto.RouteRequest;
import org.interstella.dto.RouteResponse;
import org.interstella.model.Accelerator;
import org.interstella.model.AcceleratorConnection;
import org.interstella.repository.AcceleratorRepositoryImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

class AcceleratorServiceTest {

    @Mock
    private AcceleratorRepositoryImpl acceleratorRepositoryImpl;

    @InjectMocks
    private AcceleratorServiceImpl acceleratorServiceImpl;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetCheapestRoute_Valid() {
        Accelerator accelerator1 = new Accelerator("SOL", "Sol", Arrays.asList(new AcceleratorConnection("TO", 10)));
        Accelerator accelerator2 = new Accelerator("TO", "To", Arrays.asList(new AcceleratorConnection("DEN", 20)));
        Accelerator accelerator3 = new Accelerator("DEN", "Den", Arrays.asList(new AcceleratorConnection("SOL", 30)));
        when(acceleratorRepositoryImpl.findAll()).thenReturn(Arrays.asList(accelerator1, accelerator2, accelerator3));

        RouteRequest routeRequest = new RouteRequest();
        routeRequest.setSource("SOL");
        routeRequest.setDestination("DEN");

        RouteResponse response = acceleratorServiceImpl.getCheapestRoute(routeRequest);

        List<String> expectedRoute = Arrays.asList("SOL", "TO", "DEN");
        double expectedCost = (10.0 + 20.0) * 0.10;

        assertEquals(expectedRoute, response.getRoutes());
        assertEquals(expectedCost, response.getJourneyFee());
    }

    @Test
    public void testGetCheapestRoute_NotFound() {
        Accelerator accelerator1 = new Accelerator("SOL", "Sol", Arrays.asList(new AcceleratorConnection("TO", 10)));
        Accelerator accelerator2 = new Accelerator("TO", "To", Arrays.asList(new AcceleratorConnection("DEN", 20)));
        when(acceleratorRepositoryImpl.findAll()).thenReturn(Arrays.asList(accelerator1, accelerator2));

        RouteRequest routeRequest = new RouteRequest();
        routeRequest.setSource("SOL");
        routeRequest.setDestination("Not_Existing_Destination");

        RouteResponse response = acceleratorServiceImpl.getCheapestRoute(routeRequest);

        assertEquals(null, response);
    }
}