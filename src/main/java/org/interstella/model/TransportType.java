package org.interstella.model;

public enum TransportType {
    PERSONAL_TRANSPORT("PERSONAL_TRANSPORT"),
    HTC_TRANSPORT("HTC_TRANSPORT");

    private final String value;

    TransportType(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return value;
    }
}