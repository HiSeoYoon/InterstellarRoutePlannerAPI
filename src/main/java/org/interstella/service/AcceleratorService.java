package org.interstella.service;

import org.interstella.dto.RouteRequest;
import org.interstella.dto.RouteResponse;
import org.interstella.model.Accelerator;

import java.util.List;

public interface AcceleratorService {
    List<Accelerator> getAllAccelerators();
    Accelerator getAcceleratorById(String acceleratorID);
    RouteResponse getCheapestRoute(RouteRequest request);
}
