package org.interstella.dto;

import org.interstella.model.AcceleratorConnection;

import java.util.List;

public class AcceleratorDto {
    private String id;
    private String name;
    private List<AcceleratorConnection> connections;

    public AcceleratorDto() {
    }

    public AcceleratorDto(String id, String name, List<AcceleratorConnection> connections) {
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
