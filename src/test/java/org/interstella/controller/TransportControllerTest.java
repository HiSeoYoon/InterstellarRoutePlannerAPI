package org.interstella.controller;

import org.interstella.dto.JourneyResponse;
import org.interstella.model.TransportType;
import org.interstella.service.TransportService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@WebMvcTest(TransportController.class)
public class TransportControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TransportService transportService;

    @Test
    public void testFindCheapestTransport_ValidInput() throws Exception {
        JourneyResponse response = new JourneyResponse();
        response.setVehicle(TransportType.PERSONAL_TRANSPORT);
        response.setCost(200.0);
        when(transportService.calculateCheapestTransport(any())).thenReturn(response);

        mockMvc.perform(MockMvcRequestBuilders.get("/transport/100")
                        .param("passengers", "4")
                        .param("parking", "2")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.vehicle").value("PERSONAL_TRANSPORT"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.cost").value(200.0));
    }

    @Test
    public void testFindCheapestTransport_InvalidInput() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/transport/0.0")
                        .param("passengers", "0")
                        .param("parking", "0")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }
}