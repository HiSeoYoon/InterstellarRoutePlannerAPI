package org.interstella.service;

import org.interstella.model.Accelerator;
import org.interstella.repository.AcceleratorRepositoryImpl;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AcceleratorServiceImpl implements AcceleratorService {
    private AcceleratorRepositoryImpl acceleratorRepository;

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
}
