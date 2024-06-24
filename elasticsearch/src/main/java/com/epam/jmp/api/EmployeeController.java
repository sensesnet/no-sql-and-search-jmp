package com.epam.jmp.api;

import com.epam.jmp.api.models.EmployeeDto;
import com.epam.jmp.api.models.SearchDto;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;

import java.util.List;

@Tag(name = "Employees api")
public interface EmployeeController {
    @GetMapping
    @Operation(summary = "Get all employees")
    ResponseEntity<List<EmployeeDto>> getAllEmployees();

    @GetMapping("/{id}")
    @Operation(summary = "Get employee by id")
    ResponseEntity<EmployeeDto> getEmployeeById(@PathVariable String id);

    @PostMapping
    @Operation(summary = "Create employee")
    ResponseEntity<EmployeeDto> createEmployee(@RequestBody EmployeeDto employeeDto);

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete employee by id")
    ResponseEntity<Void> deleteEmployee(@PathVariable String id);

    @PostMapping("/search")
    @Operation(summary = "Search employees by field",
            description = "In case using low level api is mget query (find multiple docs in index by id), " +
                    "in case using high level api is multiple filters search")
    ResponseEntity<List<EmployeeDto>> searchEmployees(@RequestBody SearchDto dto);

    @GetMapping("/aggregate")
    @Operation(summary = "Aggregate employees")
    ResponseEntity<String> aggregate(@RequestParam String field, @RequestParam String metricType, @RequestParam String metricField);
}
