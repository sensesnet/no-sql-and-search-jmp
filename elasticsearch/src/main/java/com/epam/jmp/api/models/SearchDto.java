package com.epam.jmp.api.models;

import java.util.List;

public class SearchDto {
    private List<SearchUnit> mappings;

    public SearchDto(List<SearchUnit> mappings) {
        this.mappings = mappings;
    }

    public SearchDto() {
    }

    public List<SearchUnit> getMappings() {
        return mappings;
    }

    public void setMappings(List<SearchUnit> mappings) {
        this.mappings = mappings;
    }
}
