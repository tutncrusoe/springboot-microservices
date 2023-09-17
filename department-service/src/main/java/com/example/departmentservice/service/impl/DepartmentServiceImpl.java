package com.example.departmentservice.service.impl;

import com.example.departmentservice.dto.DepartmentDto;
import com.example.departmentservice.entity.Department;
import com.example.departmentservice.repository.DepartmentRepository;
import com.example.departmentservice.service.DepartmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DepartmentServiceImpl implements DepartmentService {

    @Autowired
    private DepartmentRepository departmentRepository;

    @Override
    public DepartmentDto saveDepartment(DepartmentDto departmentDto) {

        Department department = Department
                .builder()
                .departmentName(departmentDto.getDepartmentName())
                .departmentDescription(departmentDto.getDepartmentDescription())
                .departmentCode(departmentDto.getDepartmentCode()).build();

        Department savedDepartment = departmentRepository.save(department);
        return DepartmentDto.builder()
                .departmentName(savedDepartment.getDepartmentName())
                .departmentDescription(savedDepartment.getDepartmentDescription())
                .departmentCode(savedDepartment.getDepartmentCode())
                .build();
    }
}
