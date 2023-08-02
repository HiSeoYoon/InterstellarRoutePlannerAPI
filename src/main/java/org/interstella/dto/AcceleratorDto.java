package org.interstella.dto;

import lombok.Data;
import org.interstella.model.AcceleratorConnection;

import java.util.List;

@Data
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
}
