package com.epam.jmp.services;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.query_dsl.MatchQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import co.elastic.clients.elasticsearch.core.GetResponse;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.elasticsearch.core.mget.MultiGetOperation;
import co.elastic.clients.elasticsearch.core.search.Hit;
import co.elastic.clients.elasticsearch.indices.Queries;
import co.elastic.clients.transport.ElasticsearchTransport;
import com.epam.jmp.api.models.EmployeeDto;
import com.epam.jmp.api.models.SearchUnit;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.util.*;

public class EmployeeServiceHighLevel implements EmployeeService{

    private final ElasticsearchClient esClient;

    public EmployeeServiceHighLevel(ElasticsearchTransport transport) {
        this.esClient = new ElasticsearchClient(transport);
    }


    @Override
    public List<EmployeeDto> getAllEmployees() {
        try {
            var response = esClient.search(s -> s.index("employees"), EmployeeDto.class);

            if (response.hits().total() != null && response.hits().total().value() != 0) {
                return response.hits().hits().stream().map(Hit::source).toList();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return Collections.emptyList();
    }

    @Override
    public EmployeeDto getEmployeeById(String id) {
        try {
            GetResponse<EmployeeDto> response = esClient.get(g -> g.index("employees").id(id), EmployeeDto.class);
            if (response.found()){
                return response.source();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    @Override
    public EmployeeDto createEmployee(EmployeeDto employeeDto) {
        try {
            var createResponse = esClient.create(c -> c.index("employees").document(employeeDto).id(employeeDto.getId()));
            if (StringUtils.equals(createResponse.result().name(), "Created"))
                return esClient.get(g -> g.index("employees").id(employeeDto.getId()), EmployeeDto.class).source();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    @Override
    public void deleteEmployee(String id) {
        try {
            esClient.delete(d -> d.index("employees").id(id));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<EmployeeDto> searchEmployees(List<SearchUnit> mappings) {
        try {
            var queries = mappings.stream().map(u -> MatchQuery.of(m -> m.field(u.getField()).query(u.getValue()))._toQuery()).toList();
            var response = esClient.search(s -> s
                            .index("employees")
                            .query(q -> q
                                    .bool(b -> b.must(queries))
                            ),
                    EmployeeDto.class
            );

            if (response.hits().total() != null && response.hits().total().value() != 0) {
                return response.hits().hits().stream().map(Hit::source).toList();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return Collections.emptyList();
    }

    @Override
    public String aggregate(String field, String metricType, String metricField) {

        Query query = MatchQuery.of(m -> m
                .field("name")
                .query(metricField)
        )._toQuery();
        SearchResponse<Void> response;
        try {
            response = esClient.search(b -> b
                            .index("employees")
                            .size(0)
                            .query(query)
                            .aggregations(field, a -> a.terms(h -> h
                                            .field(metricType)
                                    )
                            ),
                    Void.class
            );

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return response.hits().toString();
    }
}
