package com.udacity.critter.service;

import com.udacity.critter.data.Employee;
import com.udacity.critter.dto.EmployeeDTO;
import com.udacity.critter.enums.EmployeeSkill;
import com.udacity.critter.repository.EmployeeRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.DayOfWeek;
import java.util.*;

@Service
@Transactional
public class EmployeeService {
    private final EmployeeRepository employeeRepository;

    public EmployeeService(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    public EmployeeDTO save(EmployeeDTO employeeDTO) {
        Employee employee = new Employee();
        BeanUtils.copyProperties(employeeDTO, employee);
        Employee savedEmployee = employeeRepository.save(employee);
        BeanUtils.copyProperties(savedEmployee, employeeDTO);
        return employeeDTO;
    }

    public EmployeeDTO get(Long id) {
        Optional<Employee> optionalEmployee = employeeRepository.findById(id);
        Employee employee = optionalEmployee.orElse(null);
        EmployeeDTO employeeDTO = new EmployeeDTO();
        if (employee != null) {
            BeanUtils.copyProperties(employee, employeeDTO);
            return employeeDTO;
        }
        return null;
    }

    public void setAvailability(Set<DayOfWeek> daysAvailable, Long employeeId) {
        EmployeeDTO employeeDTO = get(employeeId);
        employeeDTO.setDaysAvailable(daysAvailable);
        save(employeeDTO);
    }

    public List<EmployeeDTO> getAllBySkills(Set<EmployeeSkill> skills) {
        return copyEmployeeListToEmployeeDTOList(employeeRepository.findAllBySkillsIn(Collections.singleton(skills)));
    }

    private List<EmployeeDTO> copyEmployeeListToEmployeeDTOList(List<Employee> employees) {
        ArrayList<EmployeeDTO> employeeDTOS = new ArrayList<>();
        for (Employee employee : employees) {
            EmployeeDTO employeeDTO = new EmployeeDTO();
            BeanUtils.copyProperties(employee, employeeDTO);
            employeeDTOS.add(employeeDTO);
        }
        return employeeDTOS;
    }
}
