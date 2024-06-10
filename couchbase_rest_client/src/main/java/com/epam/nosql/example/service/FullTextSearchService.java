package com.epam.nosql.example.service;

import java.util.List;

public interface FullTextSearchService {
    List<String> search(String searchTerm);
}
