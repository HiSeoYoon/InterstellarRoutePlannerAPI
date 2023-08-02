package org.interstella.model;

import lombok.Data;

@Data
public class AcceleratorConnection {
    private String id;
    private int distance;

    public AcceleratorConnection(){
    }

    public AcceleratorConnection(String id, int distance){
        this.id = id;
        this.distance = distance;
    }
}
