package org.interstella.controller;

import org.hamcrest.Matchers;
import org.interstella.dto.AcceleratorDto;
import org.interstella.dto.RouteResponse;
import org.interstella.model.AcceleratorConnection;
import org.interstella.service.AcceleratorService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@WebMvcTest(AcceleratorController.class)
class AcceleratorControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AcceleratorService acceleratorService;

    @Test
    public void testGetAllAccelerators_ValidInput() throws Exception {
        AcceleratorConnection connection1 = new AcceleratorConnection("ARC", 200);
        AcceleratorConnection connection2 = new AcceleratorConnection("DEN", 120);
        AcceleratorConnection connection3 = new AcceleratorConnection("PRO", 80);

        List<AcceleratorDto> expectedAccelerators = Arrays.asList(
                new AcceleratorDto("SOL", "Sol", Arrays.asList(connection1)),
                new AcceleratorDto("ARC", "Arcturus", Arrays.asList(connection2, connection3))
        );

        when(acceleratorService.getAllAccelerators()).thenReturn(expectedAccelerators);

        mockMvc.perform(MockMvcRequestBuilders.get("/accelerators")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$", hasSize(2)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id").value("SOL"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].name").value("Sol"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].connections.[0].id").value("ARC"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].connections.[0].distance").value("200"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].id").value("ARC"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].name").value("Arcturus"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].connections.[0].id").value("DEN"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].connections.[0].distance").value("120"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].connections.[1].id").value("PRO"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].connections.[1].distance").value("80"));
    }

    @Test
    public void testGetAllAccelerators_InvalidInput() throws Exception {
        when(acceleratorService.getAllAccelerators()).thenReturn(Collections.emptyList());

        mockMvc.perform(MockMvcRequestBuilders.get("/accelerators")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    public void testGetAccelerator_ValidInput() throws Exception {
        AcceleratorConnection connection1 = new AcceleratorConnection("ARC", 200);
        AcceleratorDto expectedAccelerator = new AcceleratorDto("SOL", "Sol", Arrays.asList(connection1));

        when(acceleratorService.getAcceleratorById("SOL")).thenReturn(expectedAccelerator);

        mockMvc.perform(MockMvcRequestBuilders.get("/accelerators/SOL")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value("SOL"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("Sol"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.connections.[0].id").value("ARC"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.connections.[0].distance").value("200"));
    }

    @Test
    public void testGetAccelerator_InvalidInput() throws Exception {
        when(acceleratorService.getAcceleratorById("NON_EXISTENT_ID")).thenReturn(null);

        mockMvc.perform(MockMvcRequestBuilders.get("/accelerators/NON_EXISTENT_ID")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    public void testGetCheapestRoute_ValidInput() throws Exception {
        AcceleratorConnection connection1 = new AcceleratorConnection("ARC", 200);
        AcceleratorConnection connection2 = new AcceleratorConnection("DEN", 120);
        AcceleratorConnection connection3 = new AcceleratorConnection("PRO", 80);

        AcceleratorDto accelerator1 = new AcceleratorDto("SOL", "Sol", Arrays.asList(connection1));
        AcceleratorDto accelerator2 = new AcceleratorDto("ARC", "Arcturus", Arrays.asList(connection2, connection3));
        AcceleratorDto accelerator3 = new AcceleratorDto("DEN", "Denebula", Collections.emptyList());
        AcceleratorDto accelerator4 = new AcceleratorDto("PRO", "Procyon", Collections.emptyList());

        List<AcceleratorDto> mockAccelerators = Arrays.asList(accelerator1, accelerator2, accelerator3, accelerator4);

        when(acceleratorService.getAllAccelerators()).thenReturn(mockAccelerators);

        RouteResponse mockResponse = new RouteResponse();
        mockResponse.setRoutes(Arrays.asList("SOL", "ARC", "DEN", "PRO"));
        mockResponse.setJourneyFee(32.5);

        when(acceleratorService.getCheapestRoute(any())).thenReturn(mockResponse);

        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.get("/accelerators/SOL/to/PRO")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.routes").isArray())
                .andExpect(MockMvcResultMatchers.jsonPath("$.routes", Matchers.hasSize(4)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.routes[0]").value("SOL"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.routes[1]").value("ARC"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.routes[2]").value("DEN"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.routes[3]").value("PRO"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.journeyFee").value(32.5));
    }

    @Test
    public void testGetCheapestRoute_InvalidInput() throws Exception {
        when(acceleratorService.getAcceleratorById(any())).thenReturn(null);

        mockMvc.perform(MockMvcRequestBuilders.get("/accelerators/NON_EXISTENT_ID/to/NON_EXISTENT_ID")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }
}