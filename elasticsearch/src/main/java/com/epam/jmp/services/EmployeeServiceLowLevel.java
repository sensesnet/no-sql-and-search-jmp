package com.epam.jmp.services;

import com.epam.jmp.api.models.EmployeeDto;
import com.epam.jmp.api.models.SearchUnit;
import com.epam.jmp.models.EmployeeCreationResponse;
import com.epam.jmp.models.EmployeeDoc;
import com.epam.jmp.models.GetAllResponse;
import com.epam.jmp.models.SearchResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.util.EntityUtils;
import org.elasticsearch.client.Request;
import org.elasticsearch.client.Response;
import org.elasticsearch.client.RestClient;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class EmployeeServiceLowLevel implements EmployeeService{

    private final RestClient restClient;
    private final ObjectMapper objectMapper;

    public EmployeeServiceLowLevel(RestClient restClient, ObjectMapper objectMapper) {
        this.restClient = restClient;
        this.objectMapper = objectMapper;
    }

    @Override
    public List<EmployeeDto> getAllEmployees() {
        try {
            Request request = new Request("GET", "/employees/_search");
            Response response = restClient.performRequest(request);
            String responseBody = EntityUtils.toString(response.getEntity());
            GetAllResponse employeeResponse = objectMapper.readValue(responseBody, GetAllResponse.class);
            return employeeResponse.getHits().getHits().stream()
                    .map(EmployeeDto::createFromHit)
                    .toList();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public EmployeeDto getEmployeeById(String id) {
        try {
            return getEmployeeDto(id);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private EmployeeDto getEmployeeDto(String id) throws IOException {
        Request request = new Request("GET", "/employees/_doc/" + id);
        Response response = restClient.performRequest(request);
        String responseBody = EntityUtils.toString(response.getEntity());
        EmployeeDoc doc = objectMapper.readValue(responseBody, EmployeeDoc.class);
        return EmployeeDto.createFromSource(doc.get_source(), doc.get_id());
    }

    @Override
    public EmployeeDto createEmployee(EmployeeDto employeeDto) {
        try {
            String json = objectMapper.writeValueAsString(employeeDto);
            Request request = new Request("POST", "/employees/_doc/" + employeeDto.getId());
            request.setJsonEntity(json);
            Response response = restClient.performRequest(request);
            String responseBody = EntityUtils.toString(response.getEntity());
            EmployeeCreationResponse doc = objectMapper.readValue(responseBody, EmployeeCreationResponse.class);

            return getEmployeeById(doc.get_id());

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void deleteEmployee(String id) {
        try {
            Request request = new Request("DELETE", "/employees/_doc/" + id);
            restClient.performRequest(request);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<EmployeeDto> searchEmployees(List<SearchUnit> mappings) {
        try {
            List<EmployeeDto> res = new ArrayList<>();
            Request request = new Request("GET", "employees/_mget");
            StringBuilder json = new StringBuilder("{\"docs\": [");
            mappings.forEach(entry -> {
                json.append("{\"").append(entry.getField()).append("\": \"").append(entry.getValue()).append("\"}");

                if (mappings.indexOf(entry) != mappings.size() - 1)
                    json.append(",");
            });
            json.append("]}");
            request.setJsonEntity(json.toString());
            Response response = restClient.performRequest(request);
            String responseBody = EntityUtils.toString(response.getEntity());
            SearchResponse doc = objectMapper.readValue(responseBody, SearchResponse.class);

            doc.getDocs().stream()
                    .filter(EmployeeDoc::isFound)
                    .forEach(document -> res.add(EmployeeDto.createFromSource(document.get_source(), document.get_id())));

            return res;

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String aggregate(String field, String metricType, String metricField) {
        try {
            Request request = new Request("GET", "/employees/_search");
            String json = String.format("{\"aggs\": {\"%s_agg\": {\"%s\": {\"field\": \"%s\"}}}}", field, metricType, metricField);
            request.setJsonEntity(json);
            Response response = restClient.performRequest(request);
            return EntityUtils.toString(response.getEntity());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
