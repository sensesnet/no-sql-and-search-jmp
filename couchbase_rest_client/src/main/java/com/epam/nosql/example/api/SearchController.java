package com.epam.nosql.example.api;

import com.couchbase.client.java.search.result.SearchRow;
import com.epam.nosql.example.dto.User;
import com.epam.nosql.example.service.FullTextSearchService;
import com.epam.nosql.example.service.FullTextSearchServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/search")
public class SearchController {

    private final FullTextSearchService searchService;

    @Autowired
    public SearchController(FullTextSearchServiceImpl searchService) {
        this.searchService = searchService;
    }

    @GetMapping("/user")
    public ResponseEntity<List<String>> fullTextSearch(@RequestParam String searchTerm) {
        return ResponseEntity
                .ok()
                .body(searchService.search(searchTerm));
    }
}
