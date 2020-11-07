package com.udacity.critter.service;

import com.udacity.critter.data.Customer;
import com.udacity.critter.data.Pet;
import com.udacity.critter.dto.PetDTO;
import com.udacity.critter.repository.CustomerRepository;
import com.udacity.critter.repository.PetRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
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

    public PetDTO get(Long petId) {
        Optional<Pet> optionalPet = petRepository.findById(petId);
        Pet pet = optionalPet.orElse(null);
        PetDTO petDTO = new PetDTO();
        if (pet != null) {
            BeanUtils.copyProperties(pet, petDTO);
            return petDTO;
        }
        return null;
    }

    public List<PetDTO> getAll() {
        return copyPetListToPetDTOList(petRepository.findAll());
    }

    public List<PetDTO> getAllByOwner(Long id) {
        Optional<Customer> optionalCustomer = customerRepository.findById(id);
        Customer customer = optionalCustomer.orElse(null);
        return copyPetListToPetDTOList(petRepository.findByOwner(customer));
    }

    public PetDTO save(PetDTO petDTO) {
        Pet pet = new Pet();
        BeanUtils.copyProperties(petDTO, pet);
        Pet savedPet = petRepository.save(pet);
        BeanUtils.copyProperties(savedPet, petDTO);
        return petDTO;
    }

    private List<PetDTO> copyPetListToPetDTOList(List<Pet> pets) {
        ArrayList<PetDTO> petDTOS = new ArrayList<>();
        for (Pet pet : pets) {
            PetDTO petDTO = new PetDTO();
            BeanUtils.copyProperties(pet, petDTO);
            petDTOS.add(petDTO);
        }
        return petDTOS;
    }
}
