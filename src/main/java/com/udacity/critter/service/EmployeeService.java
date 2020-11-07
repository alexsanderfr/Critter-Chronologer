package com.udacity.critter.service;

import com.udacity.critter.data.Employee;
import com.udacity.critter.dto.EmployeeDTO;
import com.udacity.critter.dto.EmployeeRequestDTO;
import com.udacity.critter.repository.EmployeeRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.DayOfWeek;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
public class EmployeeService {
    private final EmployeeRepository employeeRepository;

    public EmployeeService(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    public EmployeeDTO save(EmployeeDTO employeeDTO) {
        Employee savedEmployee = employeeRepository.save(copyEmployeeDTOToEmployee(employeeDTO));
        return copyEmployeeToEmployeeDTO(savedEmployee);
    }

    public EmployeeDTO get(Long id) {
        Optional<Employee> optionalEmployee = employeeRepository.findById(id);
        return optionalEmployee.map(this::copyEmployeeToEmployeeDTO).orElse(null);
    }

    public void setAvailability(Set<DayOfWeek> daysAvailable, Long employeeId) {
        EmployeeDTO employeeDTO = get(employeeId);
        employeeDTO.setDaysAvailable(daysAvailable);
        save(employeeDTO);
    }

    public List<EmployeeDTO> getAllForService(EmployeeRequestDTO employeeRequestDTO) {
        List<Employee> employees = employeeRepository
                .findAllByDaysAvailableContaining(employeeRequestDTO.getDate().getDayOfWeek())
                .stream().filter(employee -> employee.getSkills().containsAll(employeeRequestDTO.getSkills()))
                .collect(Collectors.toList());
        return copyEmployeeListToEmployeeDTOList(employees);
    }

    private List<EmployeeDTO> copyEmployeeListToEmployeeDTOList(List<Employee> employees) {
        ArrayList<EmployeeDTO> employeeDTOS = new ArrayList<>();
        for (Employee employee : employees) {
            employeeDTOS.add(copyEmployeeToEmployeeDTO(employee));
        }
        return employeeDTOS;
    }

    private Employee copyEmployeeDTOToEmployee(EmployeeDTO employeeDTO) {
        Employee employee = new Employee();
        BeanUtils.copyProperties(employeeDTO, employee);
        return employee;
    }

    private EmployeeDTO copyEmployeeToEmployeeDTO(Employee employee) {
        EmployeeDTO employeeDTO = new EmployeeDTO();
        BeanUtils.copyProperties(employee, employeeDTO);
        return employeeDTO;
    }
}
