package com.epam.nosql.example.service;

import com.couchbase.client.java.Cluster;
import com.couchbase.client.java.search.SearchQuery;
import com.couchbase.client.java.search.result.SearchResult;
import com.couchbase.client.java.search.result.SearchRow;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class FullTextSearchServiceImpl implements FullTextSearchService {

    @Autowired
    private Cluster cluster;

    public List<String> search(String searchTerm) {
        final SearchResult result = cluster.searchQuery(
                "email_index",
                SearchQuery.queryString(searchTerm)
        );

        return result.rows().stream().map(SearchRow::toString).collect(Collectors.toList());
    }
}