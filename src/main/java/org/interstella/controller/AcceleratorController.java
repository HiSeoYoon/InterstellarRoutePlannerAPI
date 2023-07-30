package org.interstella.controller;

import org.interstella.model.Accelerator;
import org.interstella.service.AcceleratorService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/accelerators")
public class AcceleratorController {
    private AcceleratorService acceleratorService;

    public AcceleratorController(AcceleratorService acceleratorService) {
        this.acceleratorService = acceleratorService;
    }

    @GetMapping
    public ResponseEntity<List<Accelerator>> getAllAccelerators() {
        List<Accelerator> accelerators = acceleratorService.getAllAccelerators();

        if (accelerators.size() == 0) {
            return ResponseEntity.notFound().build();
        } else {
            return ResponseEntity.ok(accelerators);
        }
    }

    @GetMapping("/{acceleratorID}")
    public ResponseEntity<Accelerator> getAccelerator(@PathVariable String acceleratorID) {
        Accelerator accelerator = acceleratorService.getAcceleratorById(acceleratorID);

        if (accelerator != null) {
            return ResponseEntity.ok(accelerator);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
