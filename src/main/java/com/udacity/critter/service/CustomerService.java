package com.udacity.critter.service;

import com.udacity.critter.data.Customer;
import com.udacity.critter.data.Pet;
import com.udacity.critter.dto.CustomerDTO;
import com.udacity.critter.repository.CustomerRepository;
import com.udacity.critter.repository.PetRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
@Transactional
public class CustomerService {
    private final CustomerRepository customerRepository;
    private final PetRepository petRepository;

    public CustomerService(CustomerRepository customerRepository, PetRepository petRepository) {
        this.customerRepository = customerRepository;
        this.petRepository = petRepository;
    }

    public CustomerDTO save(CustomerDTO customerDTO) {
        Customer customer = new Customer();
        BeanUtils.copyProperties(customerDTO, customer);
        ArrayList<Pet> pets = new ArrayList<>();
        if (customerDTO.getPetIds() != null) {
            for (Long petId : customerDTO.getPetIds()) {
                Optional<Pet> optionalPet = petRepository.findById(petId);
                optionalPet.ifPresent(pets::add);
            }
            customer.setPets(pets);
        }
        Customer savedCustomer = customerRepository.save(customer);
        CustomerDTO savedCustomerDTO = new CustomerDTO();
        BeanUtils.copyProperties(savedCustomer, savedCustomerDTO);
        if (savedCustomer.getPets() != null) {
            savedCustomerDTO.setPetIds(savedCustomer.getPets().stream().map(Pet::getId).collect(Collectors.toList()));
        }
        return savedCustomerDTO;
    }

    public List<CustomerDTO> getAll() {
        return copyCustomerListToCustomerDTOList(customerRepository.findAll());
    }

    public CustomerDTO getByPetId(Long id) {
        Optional<Pet> optionalPet = petRepository.findById(id);
        Pet pet = optionalPet.orElse(null);
        Customer customer = customerRepository.findByPetsContains(pet);
        CustomerDTO customerDTO = new CustomerDTO();
        BeanUtils.copyProperties(customer, customerDTO);
        if (customer.getPets() != null) {
            customerDTO.setPetIds(customer.getPets().stream().map(Pet::getId).collect(Collectors.toList()));
        }
        return customerDTO;
    }

    private List<CustomerDTO> copyCustomerListToCustomerDTOList(List<Customer> customers) {
        ArrayList<CustomerDTO> customerDTOS = new ArrayList<>();
        for (Customer customer : customers) {
            CustomerDTO customerDTO = new CustomerDTO();
            BeanUtils.copyProperties(customer, customerDTO);
            if (customer.getPets() != null) {
                customerDTO.setPetIds(customer.getPets().stream().map(Pet::getId).collect(Collectors.toList()));
            }
            customerDTOS.add(customerDTO);
        }
        return customerDTOS;
    }
}
