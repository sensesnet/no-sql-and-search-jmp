package com.epam.jmp.models;

import java.util.List;

public class SearchResponse {
    private List<EmployeeDoc> docs;

    public List<EmployeeDoc> getDocs() {
        return docs;
    }

    public void setDocs(List<EmployeeDoc> docs) {
        this.docs = docs;
    }
}
