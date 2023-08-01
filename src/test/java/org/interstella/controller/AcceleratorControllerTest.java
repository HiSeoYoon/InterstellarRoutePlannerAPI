package org.interstella.controller;

import org.hamcrest.Matchers;
import org.interstella.dto.RouteResponse;
import org.interstella.model.Accelerator;
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

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@WebMvcTest(AcceleratorController.class)
class AcceleratorControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AcceleratorService acceleratorService;

    @Test
    public void testGetCheapestRoute_ValidInput() throws Exception {
        AcceleratorConnection connection1 = new AcceleratorConnection("ARC", 200);
        AcceleratorConnection connection2 = new AcceleratorConnection("DEN", 120);
        AcceleratorConnection connection3 = new AcceleratorConnection("PRO", 80);

        Accelerator accelerator1 = new Accelerator("SOL", "Sol", Arrays.asList(connection1));
        Accelerator accelerator2 = new Accelerator("ARC", "Arcturus", Arrays.asList(connection2, connection3));
        Accelerator accelerator3 = new Accelerator("DEN", "Denebula", Collections.emptyList());
        Accelerator accelerator4 = new Accelerator("PRO", "Procyon", Collections.emptyList());

        List<Accelerator> mockAccelerators = Arrays.asList(accelerator1, accelerator2, accelerator3, accelerator4);

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