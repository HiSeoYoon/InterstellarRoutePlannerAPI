package org.interstella.model;

public class AcceleratorConnection {
    private String id;
    private int distance;

    public AcceleratorConnection(){
    }

    public AcceleratorConnection(String id, int distance){
        this.id = id;
        this.distance = distance;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getDistance() {
        return distance;
    }

    public void setDistance(int distance) {
        this.distance = distance;
    }
}
