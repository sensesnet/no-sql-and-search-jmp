package com.epam.jmp.services;

import com.epam.jmp.api.models.EmployeeDto;
import com.epam.jmp.api.models.SearchUnit;

import java.util.List;

public interface EmployeeService {
    List<EmployeeDto> getAllEmployees();

    EmployeeDto getEmployeeById(String id);

    EmployeeDto createEmployee(EmployeeDto employeeDto);

    void deleteEmployee(String id);

    List<EmployeeDto> searchEmployees(List<SearchUnit> mappings);

    String aggregate(String field, String metricType, String metricField);
}
