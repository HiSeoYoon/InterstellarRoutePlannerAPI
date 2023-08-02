package org.interstella.model;

import lombok.Data;

import java.util.List;

@Data
public class Accelerator {
    private String id;
    private String name;
    private List<AcceleratorConnection> connections;

    public Accelerator() {
    }

    public Accelerator(String id, String name, List<AcceleratorConnection> connections) {
        this.id = id;
        this.name = name;
        this.connections = connections;
    }
}
