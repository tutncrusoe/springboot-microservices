package com.example.employeeservice.controller;

import com.example.employeeservice.dto.DepartmentDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(url = "http://localhost:8080", value = "department-service")
public interface APIClient {

    @GetMapping("api/departments/{department-code}")
    public DepartmentDto getDepartmentByCode(@PathVariable("department-code") String code);
}
