package org.interstella.model;

import java.util.List;

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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<AcceleratorConnection> getConnections() {
        return connections;
    }

    public void setConnections(List<AcceleratorConnection> connections) {
        this.connections = connections;
    }
}
