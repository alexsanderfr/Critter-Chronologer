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
        Pet pet = petRepository.getOne(petId);
        return copyPetToPetDTO(pet);
    }

    public List<PetDTO> getAll() {
        return copyPetListToPetDTOList(petRepository.findAll());
    }

    public List<PetDTO> getAllByOwner(Long id) {
        Customer customer = customerRepository.getOne(id);
        return copyPetListToPetDTOList(petRepository.findByOwner(customer));
    }

    public PetDTO save(PetDTO petDTO) {
        Pet savedPet = petRepository.save(copyPetDTOtoPet(petDTO));
        Optional<Customer> optionalCustomer = customerRepository.findById(petDTO.getOwnerId());
        if (optionalCustomer.isPresent()) {
            Customer customer = optionalCustomer.get();
            ArrayList<Pet> pets = (ArrayList<Pet>) customer.getPets();
            if (pets == null) pets = new ArrayList<>();
            pets.add(savedPet);
            customer.setPets(pets);
        }
        return copyPetToPetDTO(savedPet);
    }

    private List<PetDTO> copyPetListToPetDTOList(List<Pet> pets) {
        ArrayList<PetDTO> petDTOS = new ArrayList<>();
        for (Pet pet : pets) {
            petDTOS.add(copyPetToPetDTO(pet));
        }
        return petDTOS;
    }

    private Pet copyPetDTOtoPet(PetDTO petDTO) {
        Pet pet = new Pet();
        BeanUtils.copyProperties(petDTO, pet);
        Optional<Customer> optionalCustomer = customerRepository.findById(petDTO.getOwnerId());
        optionalCustomer.ifPresent(pet::setOwner);
        return pet;
    }

    private PetDTO copyPetToPetDTO(Pet pet) {
        PetDTO petDTO = new PetDTO();
        BeanUtils.copyProperties(pet, petDTO);
        if (pet.getOwner() != null) {
            petDTO.setOwnerId(pet.getOwner().getId());
        }
        return petDTO;
    }
}
