package com.example.employeeservice.service.impl;

import com.example.employeeservice.client.APIClient;
import com.example.employeeservice.dto.APIResponseDto;
import com.example.employeeservice.dto.DepartmentDto;
import com.example.employeeservice.dto.EmployeeDto;
import com.example.employeeservice.entity.Employee;
import com.example.employeeservice.repository.EmployeeRepository;
import com.example.employeeservice.service.EmployeeService;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import javax.transaction.Transactional;

@Service
@Transactional
public class EmployeeServiceImpl implements EmployeeService {

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private APIClient apiClient;

    @Autowired
    private WebClient webClient;

    @Autowired
    private static final Logger LOGGER = LoggerFactory.getLogger(EmployeeServiceImpl.class);

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

    //    @CircuitBreaker(name = "{spring.application.name}", fallbackMethod = "getDefaultDepartment")
    @Retry(name = "{spring.application.name}", fallbackMethod = "getDefaultDepartment")
    @Override
    public APIResponseDto getEmployeeById(Long id) {

        LOGGER.info("inside getEmployeeById() method");
        Employee employee = employeeRepository.findById(id).get();

//        DepartmentDto departmentDto = apiClient.getDepartmentByCode(employee.getDepartmentCode());

        DepartmentDto departmentDto = webClient
                .get()
                .uri("http://localhost:8080/api/departments/" + employee.getDepartmentCode())
                .retrieve()
                .bodyToMono(DepartmentDto.class)
                .block();

        return getApiResponseDto(employee, departmentDto);
    }

    private APIResponseDto getDefaultDepartment(Long id, Exception exception) {

        LOGGER.info("inside getDefaultDepartment() method");
        Employee employee = employeeRepository.findById(id).get();

        DepartmentDto departmentDto = new DepartmentDto();
        departmentDto.setId(1L);
        departmentDto.setDepartmentCode("default");
        departmentDto.setDepartmentName("default");
        departmentDto.setDepartmentDescription("default");

        return getApiResponseDto(employee, departmentDto);
    }

    @NotNull
    private static APIResponseDto getApiResponseDto(Employee employee, DepartmentDto departmentDto) {
        EmployeeDto employeeDto = new EmployeeDto();
        employeeDto.setId(employee.getId());
        employeeDto.setEmail(employee.getEmail());
        employeeDto.setFirstName(employee.getFirstName());
        employeeDto.setLastName(employee.getLastName());
        employeeDto.setDepartmentCode(employee.getDepartmentCode());
        employeeDto.setOrganizationCode(employee.getOrganizationCode());

        APIResponseDto apiResponseDto = new APIResponseDto();
        apiResponseDto.setEmployeeDto(employeeDto);
        apiResponseDto.setDepartmentDto(departmentDto);
        return apiResponseDto;
    }
}
