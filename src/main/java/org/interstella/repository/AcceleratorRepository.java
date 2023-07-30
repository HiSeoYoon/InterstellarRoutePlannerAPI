package org.interstella.repository;

import org.interstella.model.Accelerator;

import java.util.List;

public interface AcceleratorRepository {
    List<Accelerator> findAll();
    Accelerator findById(String acceleratorID);
}
