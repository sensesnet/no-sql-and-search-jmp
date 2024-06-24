package com.epam.jmp.api;

import co.elastic.clients.json.jackson.JacksonJsonpMapper;
import co.elastic.clients.transport.rest_client.RestClientTransport;
import com.epam.jmp.api.models.EmployeeDto;
import com.epam.jmp.api.models.SearchDto;
import com.epam.jmp.configs.ElasticsearchConfig;
import com.epam.jmp.services.EmployeeService;
import com.epam.jmp.services.EmployeeServiceHighLevel;
import com.epam.jmp.services.EmployeeServiceLowLevel;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.elasticsearch.client.RestClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/employees")
public class EmployeeControllerImpl implements EmployeeController {

    private final EmployeeService employeeService;

    public EmployeeControllerImpl(ElasticsearchConfig config, RestClient restClient, ObjectMapper objectMapper) {

        if (config.useLowLevel()){
            this.employeeService = new EmployeeServiceLowLevel(restClient, objectMapper);
        } else
            this.employeeService = new EmployeeServiceHighLevel(new RestClientTransport(
                    restClient, new JacksonJsonpMapper()));

    }

    @Override
    @GetMapping
    public ResponseEntity<List<EmployeeDto>> getAllEmployees() {
        return ResponseEntity.ok(employeeService.getAllEmployees());
    }

    @Override
    @GetMapping("/{id}")
    public ResponseEntity<EmployeeDto> getEmployeeById(@PathVariable String id) {
        return ResponseEntity.ok(employeeService.getEmployeeById(id));
    }

    @Override
    @PostMapping
    public ResponseEntity<EmployeeDto> createEmployee(@RequestBody EmployeeDto employeeDto) {
        return ResponseEntity.ok(employeeService.createEmployee(employeeDto));
    }

    @Override
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEmployee(@PathVariable String id) {
        employeeService.deleteEmployee(id);
        return ResponseEntity.noContent().build();
    }

    @Override
    @PostMapping("/search")
    public ResponseEntity<List<EmployeeDto>> searchEmployees(@RequestBody SearchDto dto) {
        return ResponseEntity.ok(employeeService.searchEmployees(dto.getMappings()));
    }

    @Override
    @GetMapping("/aggregate")
    public ResponseEntity<String> aggregate(@RequestParam String field, @RequestParam String metricType, @RequestParam String metricField) {
        return ResponseEntity.ok(employeeService.aggregate(field, metricType, metricField));
    }
}
