package com.udacity.critter.service;

import com.udacity.critter.data.Customer;
import com.udacity.critter.data.Employee;
import com.udacity.critter.data.Pet;
import com.udacity.critter.data.Schedule;
import com.udacity.critter.repository.CustomerRepository;
import com.udacity.critter.repository.EmployeeRepository;
import com.udacity.critter.repository.PetRepository;
import com.udacity.critter.repository.ScheduleRepository;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

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

    public void save(Schedule schedule) {
        scheduleRepository.save(schedule);
    }

    public List<Schedule> getAll() {
        return scheduleRepository.findAll();
    }

    public List<Schedule> getAllByEmployee(Long employeeId) {
        Optional<Employee> optionalEmployee = employeeRepository.findById(employeeId);
        Employee employee = optionalEmployee.orElse(null);
        return scheduleRepository.findByEmployeesContaining(employee);
    }

    public List<Schedule> getAllByCustomer(Long customerId) {
        Optional<Customer> optionalCustomer = customerRepository.findById(customerId);
        Customer customer = optionalCustomer.orElse(null);
        Collection<List<Pet>> pets = Collections.singleton(customer != null ? customer.getPets() : null);
        return scheduleRepository.findByPetsIn(pets);
    }

    public List<Schedule> getAllByPet(Long petId) {
        Optional<Pet> optionalPet = petRepository.findById(petId);
        Pet pet = optionalPet.orElse(null);
        return scheduleRepository.findByPetsContaining(pet);
    }
}
