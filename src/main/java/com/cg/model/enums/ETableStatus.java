package com.cg.model.enums;

public enum ETableStatus {
    EMPTY("EMPTY"),
    BUSY("BUSY");

    private final String value;

    ETableStatus(String value) {
        this.value = value;
    }

    public String getValue() {
        return this.value;
    }


}
