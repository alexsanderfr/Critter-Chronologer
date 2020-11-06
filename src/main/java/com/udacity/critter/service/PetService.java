package com.udacity.critter.service;

import com.udacity.critter.data.Customer;
import com.udacity.critter.data.Pet;
import com.udacity.critter.repository.CustomerRepository;
import com.udacity.critter.repository.PetRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;


@Service
@Transactional
public class PetService {
    private final PetRepository petRepository;
    private final CustomerRepository customerRepository;

    public PetService(PetRepository petRepository, CustomerRepository customerRepository) {
        this.petRepository = petRepository;
        this.customerRepository = customerRepository;
    }

    public Pet get(Long petId) {
        Optional<Pet> optionalPet = petRepository.findById(petId);
        return optionalPet.orElse(null);
    }

    public List<Pet> getAll() {
        return petRepository.findAll();
    }

    public List<Pet> getAllByOwner(Long id) {
        Optional<Customer> optionalCustomer = customerRepository.findById(id);
        Customer customer = optionalCustomer.orElse(null);
        return petRepository.findByOwner(customer);
    }

    public void save(Pet pet) {
        petRepository.save(pet);
    }


}
