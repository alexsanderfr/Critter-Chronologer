package com.udacity.critter.service;

import com.udacity.critter.data.Customer;
import com.udacity.critter.data.Employee;
import com.udacity.critter.data.Pet;
import com.udacity.critter.data.Schedule;
import com.udacity.critter.dto.ScheduleDTO;
import com.udacity.critter.repository.CustomerRepository;
import com.udacity.critter.repository.EmployeeRepository;
import com.udacity.critter.repository.PetRepository;
import com.udacity.critter.repository.ScheduleRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
public class ScheduleService {
    private final ScheduleRepository scheduleRepository;
    private final PetRepository petRepository;
    private final EmployeeRepository employeeRepository;
    private final CustomerRepository customerRepository;

    public ScheduleService(ScheduleRepository scheduleRepository, PetRepository petRepository, EmployeeRepository employeeRepository, CustomerRepository customerRepository) {
        this.scheduleRepository = scheduleRepository;
        this.petRepository = petRepository;
        this.employeeRepository = employeeRepository;
        this.customerRepository = customerRepository;
    }

    public ScheduleDTO save(ScheduleDTO scheduleDTO) {
        Schedule schedule = new Schedule();
        BeanUtils.copyProperties(scheduleDTO, schedule);
        if (scheduleDTO.getPetIds() != null) {
            ArrayList<Pet> pets = new ArrayList<>();
            for (Long petId : scheduleDTO.getPetIds()) {
                Optional<Pet> optionalPet = petRepository.findById(petId);
                optionalPet.ifPresent(pets::add);
            }
            schedule.setPets(pets);
        }
        if (scheduleDTO.getEmployeeIds() != null) {
            ArrayList<Employee> employees = new ArrayList<>();
            for (Long employeeId : scheduleDTO.getEmployeeIds()) {
                Optional<Employee> optionalEmployee = employeeRepository.findById(employeeId);
                optionalEmployee.ifPresent(employees::add);
            }
            schedule.setEmployees(employees);
        }
        Schedule savedSchedule = scheduleRepository.save(schedule);
        BeanUtils.copyProperties(savedSchedule, scheduleDTO);
        scheduleDTO.setPetIds(savedSchedule.getPets().stream().map(Pet::getId).collect(Collectors.toList()));
        scheduleDTO.setEmployeeIds(savedSchedule.getEmployees().stream().map(Employee::getId).collect(Collectors.toList()));
        return scheduleDTO;
    }

    public List<ScheduleDTO> getAll() {
        return copyScheduleListToScheduleDTOList(scheduleRepository.findAll());
    }

    public List<ScheduleDTO> getAllByEmployee(Long employeeId) {
        Optional<Employee> optionalEmployee = employeeRepository.findById(employeeId);
        Employee employee = optionalEmployee.orElse(null);
        return copyScheduleListToScheduleDTOList(scheduleRepository.findByEmployeesContains(employee));
    }

    public List<ScheduleDTO> getAllByCustomer(Long customerId) {
        Optional<Customer> optionalCustomer = customerRepository.findById(customerId);
        Customer customer = optionalCustomer.orElse(null);
        Collection<List<Pet>> pets = Collections.singleton(customer != null ? customer.getPets() : null);
        return copyScheduleListToScheduleDTOList(scheduleRepository.findByPetsIn(pets));
    }

    public List<ScheduleDTO> getAllByPet(Long petId) {
        Optional<Pet> optionalPet = petRepository.findById(petId);
        Pet pet = optionalPet.orElse(null);
        return copyScheduleListToScheduleDTOList(scheduleRepository.findByPetsContains(pet));
    }

    private List<ScheduleDTO> copyScheduleListToScheduleDTOList(List<Schedule> schedules) {
        ArrayList<ScheduleDTO> scheduleDTOS = new ArrayList<>();
        for (Schedule schedule : schedules) {
            ScheduleDTO scheduleDTO = new ScheduleDTO();
            BeanUtils.copyProperties(schedule, scheduleDTO);
            scheduleDTO.setPetIds(schedule.getPets().stream().map(Pet::getId)
                    .collect(Collectors.toList()));
            scheduleDTO.setEmployeeIds(schedule.getEmployees().stream().map(Employee::getId)
                    .collect(Collectors.toList()));
            scheduleDTOS.add(scheduleDTO);
        }
        return scheduleDTOS;
    }
}
