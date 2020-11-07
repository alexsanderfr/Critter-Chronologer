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
        Customer savedCustomer = customerRepository.save(copyCustomerDTOtoCustomer(customerDTO));
        return copyCustomerToCustomerDTO(savedCustomer);
    }

    public List<CustomerDTO> getAll() {
        return copyCustomerListToCustomerDTOList(customerRepository.findAll());
    }

    public CustomerDTO getByPetId(Long id) {
        Pet pet = petRepository.getOne(id);
        Customer customer = pet.getOwner();
        return copyCustomerToCustomerDTO(customer);
    }

    public CustomerDTO copyCustomerToCustomerDTO(Customer customer) {
        CustomerDTO customerDTO = new CustomerDTO();
        BeanUtils.copyProperties(customer, customerDTO);
        if (customer.getPets() != null && !customer.getPets().isEmpty()) {
            customerDTO.setPetIds(customer.getPets().stream().map(Pet::getId).collect(Collectors.toList()));
        }
        return customerDTO;
    }

    public Customer copyCustomerDTOtoCustomer(CustomerDTO customerDTO) {
        Customer customer = new Customer();
        BeanUtils.copyProperties(customerDTO, customer);
        if (customerDTO.getPetIds() != null && !customerDTO.getPetIds().isEmpty()) {
            ArrayList<Pet> pets = new ArrayList<>();
            for (Long petId : customerDTO.getPetIds()) {
                Pet pet = petRepository.getOne(petId);
                pets.add(pet);
            }
            customer.setPets(pets);
        }
        return customer;
    }

    private List<CustomerDTO> copyCustomerListToCustomerDTOList(List<Customer> customers) {
        ArrayList<CustomerDTO> customerDTOS = new ArrayList<>();
        for (Customer customer : customers) {
            customerDTOS.add(copyCustomerToCustomerDTO(customer));
        }
        return customerDTOS;
    }
}
