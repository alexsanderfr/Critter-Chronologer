package com.udacity.critter.controller;

import com.udacity.critter.data.Pet;
import com.udacity.critter.dto.PetDTO;
import com.udacity.critter.service.PetService;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Handles web requests related to Pets.
 */
@RestController
@RequestMapping("/pet")
public class PetController {
    private final PetService petService;

    public PetController(PetService petService) {
        this.petService = petService;
    }

    @PostMapping
    public PetDTO savePet(@RequestBody PetDTO petDTO) {
        Pet pet = new Pet();
        BeanUtils.copyProperties(petDTO, pet);
        petService.save(pet);
        return petDTO;
    }

    @GetMapping("/{petId}")
    public PetDTO getPet(@PathVariable long petId) {
        Pet pet = petService.get(petId);
        PetDTO petDTO = new PetDTO();
        BeanUtils.copyProperties(pet, petDTO);
        return petDTO;
    }

    @GetMapping
    public List<PetDTO> getPets() {
        List<Pet> pets = petService.getAll();
        return copyPetListToPetDTOList(pets);
    }

    @GetMapping("/owner/{ownerId}")
    public List<PetDTO> getPetsByOwner(@PathVariable long ownerId) {
        List<Pet> pets = petService.getAllByOwner(ownerId);
        return copyPetListToPetDTOList(pets);
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
