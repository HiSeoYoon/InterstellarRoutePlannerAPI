package org.interstella.service;

import org.interstella.dto.AcceleratorDto;
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
import org.springframework.dao.DataAccessException;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
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
    public void testGetAllAccelerators_Valid() {
        AcceleratorConnection connection1 = new AcceleratorConnection("ARC", 200);
        AcceleratorConnection connection2 = new AcceleratorConnection("DEN", 120);
        AcceleratorConnection connection3 = new AcceleratorConnection("PRO", 80);

        List<Accelerator> expectedAccelerators = Arrays.asList(
                new Accelerator("SOL", "Sol", Arrays.asList(connection1)),
                new Accelerator("ARC", "Arcturus", Arrays.asList(connection2, connection3))
        );

        when(acceleratorRepositoryImpl.findAll()).thenReturn(expectedAccelerators);

        List<AcceleratorDto> result = acceleratorServiceImpl.getAllAccelerators();

        for (int i = 0; i < expectedAccelerators.size(); i++) {
            Accelerator expectedAcc = expectedAccelerators.get(i);
            AcceleratorDto actualDto = result.get(i);

            assertEquals(expectedAcc.getId(), actualDto.getId());
            assertEquals(expectedAcc.getName(), actualDto.getName());
            assertEquals(expectedAcc.getConnections(), actualDto.getConnections());
        }
    }

    @Test
    public void testGetAllAccelerators_DataAccessException() {
        when(acceleratorRepositoryImpl.findAll()).thenThrow(new DataAccessException("Test exception") {
        });

        assertThrows(RuntimeException.class, () -> acceleratorServiceImpl.getAllAccelerators());
    }

    @Test
    public void testGetAllAccelerators_NoAcceleratorsFound() {
        when(acceleratorRepositoryImpl.findAll()).thenReturn(null);

        assertThrows(RuntimeException.class, () -> acceleratorServiceImpl.getAllAccelerators());
    }

    @Test
    public void testGetAcceleratorById_Valid() {
        AcceleratorConnection connection1 = new AcceleratorConnection("ARC", 200);
        Accelerator expectedAccelerator = new Accelerator("SOL", "Sol", Arrays.asList(connection1));

        when(acceleratorRepositoryImpl.findById("SOL")).thenReturn(expectedAccelerator);

        AcceleratorDto result = acceleratorServiceImpl.getAcceleratorById("SOL");

        assertEquals(expectedAccelerator.getId(), result.getId());
        assertEquals(expectedAccelerator.getName(), result.getName());
        assertEquals(expectedAccelerator.getConnections(), result.getConnections());
    }

    @Test
    public void testGetAcceleratorById_DataAccessException() {
        when(acceleratorRepositoryImpl.findById("SOL")).thenThrow(new DataAccessException("Test exception") {
        });

        assertThrows(RuntimeException.class, () -> acceleratorServiceImpl.getAcceleratorById("SOL"));
    }

    @Test
    public void testGetAcceleratorById_NoAcceleratorFound() {
        when(acceleratorRepositoryImpl.findById("NON_EXISTENT_ID")).thenReturn(null);

        assertThrows(RuntimeException.class, () -> acceleratorServiceImpl.getAcceleratorById("NON_EXISTENT_ID"));
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