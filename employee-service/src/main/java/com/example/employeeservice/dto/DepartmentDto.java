package com.example.employeeservice.dto;

import lombok.*;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DepartmentDto {

    private Long id;

    private String departmentName;

    private String departmentDescription;

    private String departmentCode;
}
