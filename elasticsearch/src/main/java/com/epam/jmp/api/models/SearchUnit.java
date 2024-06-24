package com.epam.jmp.api.models;

public class SearchUnit {
    private String field;
    private String value;

    public SearchUnit(String field, String value) {
        this.field = field;
        this.value = value;
    }

    public SearchUnit() {
    }

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
