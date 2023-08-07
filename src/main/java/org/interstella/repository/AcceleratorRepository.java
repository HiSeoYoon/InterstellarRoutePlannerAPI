package org.interstella.repository;

import org.interstella.model.Accelerator;
import org.interstella.model.AcceleratorRoute;

import java.util.List;

public interface AcceleratorRepository {
    List<Accelerator> findAll();
    Accelerator findById(String acceleratorID);
    int insertRoute(AcceleratorRoute acceleratorRoute);
    AcceleratorRoute findBySourceAndDestination(String sourceAcceleratorId, String targetAcceleratorId);
}
