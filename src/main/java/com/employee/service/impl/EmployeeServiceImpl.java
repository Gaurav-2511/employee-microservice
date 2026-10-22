package com.employee.service.impl;

import com.employee.model.dto.EmployeeDto;
import com.employee.model.entity.Employee;
import com.employee.repository.EmployeeRepository;
import com.employee.service.EmployeeService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final ModelMapper modelMapper;

    public EmployeeServiceImpl(EmployeeRepository employeeRepository, ModelMapper modelMapper) {
        this.employeeRepository = employeeRepository;
        this.modelMapper = modelMapper;
    }


    @Override
    public EmployeeDto saveEmployee(EmployeeDto employeeDto) {
        if (employeeDto.getId() != null) {
            throw new RuntimeException("Employee already exists");
        }
        Employee entity = modelMapper.map(employeeDto, Employee.class);
        Employee savedEntity = employeeRepository.save(entity);
        return modelMapper.map(savedEntity, EmployeeDto.class);

    }

    @Override
    public EmployeeDto updateEmployee(Long id, EmployeeDto employeeDto) {
        if (id == null || employeeDto.getId() == null) {
            throw new RuntimeException("Please provide an employee id");
        }

        if (!Objects.equals(id, employeeDto.getId())) {
            throw new RuntimeException("Ids don't match");
        }

        employeeRepository.findById(id).orElseThrow(() -> new RuntimeException("Employee not found"));

        Employee entity = modelMapper.map(employeeDto, Employee.class);
        Employee updateEmployee = employeeRepository.save(entity);
        return modelMapper.map(updateEmployee, EmployeeDto.class);
    }

    @Override
    public void deleteEmployee(Long id) {
        Employee employee = employeeRepository.findById(id).orElseThrow(() -> new RuntimeException("Employee not found"));
        employeeRepository.delete(employee);
    }

    @Override
    public EmployeeDto getSingleEmployee(Long id) {
        Employee employee = employeeRepository.findById(id).orElseThrow(() -> new RuntimeException("Employee not found"));
        return modelMapper.map(employee, EmployeeDto.class);
    }

    @Override
    public List<EmployeeDto> getAllEmployees() {
        List<Employee> employees = employeeRepository.findAll();
        return employees.stream().map(emp -> modelMapper.map(emp, EmployeeDto.class)).toList();
    }
}
