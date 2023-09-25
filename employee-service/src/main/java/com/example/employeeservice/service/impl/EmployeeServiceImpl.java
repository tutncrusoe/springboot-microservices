package com.example.employeeservice.service.impl;

import com.example.employeeservice.dto.EmployeeDto;
import com.example.employeeservice.entity.Employee;
import com.example.employeeservice.repository.EmployeeRepository;
import com.example.employeeservice.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import javax.xml.bind.ValidationException;
import java.util.Objects;
import java.util.Optional;

@Service
@Transactional
public class EmployeeServiceImpl implements EmployeeService {

    @Autowired
    private EmployeeRepository employeeRepository;

    @Override
    public EmployeeDto saveEmployee(EmployeeDto employeeDto) {

        Employee employee = Employee.builder()
                .firstName(employeeDto.getFirstName())
                .lastName(employeeDto.getLastName())
                .email(employeeDto.getEmail())
                .departmentCode(employeeDto.getDepartmentCode())
                .organizationCode(employeeDto.getOrganizationCode())
                .build();

        Employee saveDEmployee = employeeRepository.save(employee);


        return EmployeeDto.builder()
                .id(saveDEmployee.getId())
                .firstName(saveDEmployee.getFirstName())
                .lastName(saveDEmployee.getLastName())
                .email(employeeDto.getEmail())
                .departmentCode(employeeDto.getDepartmentCode())
                .organizationCode(employeeDto.getOrganizationCode())
                .build();
    }

    @Override
    public EmployeeDto getEmployeeById(Long id) {
        Employee employee = employeeRepository.findById(id).get();
        return EmployeeDto.builder()
                .id(employee.getId())
                .firstName(employee.getEmail())
                .lastName(employee.getLastName())
                .email(employee.getEmail())
                .departmentCode(employee.getDepartmentCode())
                .organizationCode(employee.getOrganizationCode())
                .build();
    }
}
