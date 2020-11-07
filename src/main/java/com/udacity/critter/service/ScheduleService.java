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
        Schedule savedSchedule = scheduleRepository.save(copyScheduleDTOToSchedule(scheduleDTO));
        return copyScheduleToScheduleDTO(savedSchedule);
    }

    public List<ScheduleDTO> getAll() {
        return copyScheduleListToScheduleDTOList(scheduleRepository.findAll());
    }

    public List<ScheduleDTO> getAllByEmployee(Long employeeId) {
        Employee employee = employeeRepository.getOne(employeeId);
        return copyScheduleListToScheduleDTOList(scheduleRepository.findByEmployeesContains(employee));
    }

    public List<ScheduleDTO> getAllByCustomer(Long customerId) {
        Customer customer = customerRepository.getOne(customerId);
        Collection<List<Pet>> pets = Collections.singleton(customer.getPets());
        return copyScheduleListToScheduleDTOList(scheduleRepository.findByPetsIn(pets));
    }

    public List<ScheduleDTO> getAllByPet(Long petId) {
        Pet pet = petRepository.getOne(petId);
        return copyScheduleListToScheduleDTOList(scheduleRepository.findByPetsContains(pet));
    }

    private List<ScheduleDTO> copyScheduleListToScheduleDTOList(List<Schedule> schedules) {
        ArrayList<ScheduleDTO> scheduleDTOS = new ArrayList<>();
        for (Schedule schedule : schedules) {
            scheduleDTOS.add(copyScheduleToScheduleDTO(schedule));
        }
        return scheduleDTOS;
    }

    private ScheduleDTO copyScheduleToScheduleDTO(Schedule schedule) {
        ScheduleDTO scheduleDTO = new ScheduleDTO();
        BeanUtils.copyProperties(schedule, scheduleDTO);
        scheduleDTO.setPetIds(schedule.getPets().stream().map(Pet::getId).collect(Collectors.toList()));
        scheduleDTO.setEmployeeIds(schedule.getEmployees().stream().map(Employee::getId).collect(Collectors.toList()));
        return scheduleDTO;
    }

    private Schedule copyScheduleDTOToSchedule(ScheduleDTO scheduleDTO) {
        Schedule schedule = new Schedule();
        BeanUtils.copyProperties(scheduleDTO, schedule);
        if (scheduleDTO.getPetIds() != null && !scheduleDTO.getPetIds().isEmpty()) {
            ArrayList<Pet> pets = new ArrayList<>();
            for (Long petId : scheduleDTO.getPetIds()) {
                Pet pet = petRepository.getOne(petId);
                pets.add(pet);
            }
            schedule.setPets(pets);
        }
        if (scheduleDTO.getEmployeeIds() != null && !scheduleDTO.getEmployeeIds().isEmpty()) {
            ArrayList<Employee> employees = new ArrayList<>();
            for (Long employeeId : scheduleDTO.getEmployeeIds()) {
                Employee employee = employeeRepository.getOne(employeeId);
                employees.add(employee);
            }
            schedule.setEmployees(employees);
        }
        return schedule;
    }
}
