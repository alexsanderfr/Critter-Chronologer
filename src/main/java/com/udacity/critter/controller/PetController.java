package com.udacity.critter.controller;

import com.udacity.critter.data.Customer;
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
        return petService.save(petDTO);
    }

    @GetMapping("/{petId}")
    public PetDTO getPet(@PathVariable long petId) {
        return petService.get(petId);
    }

    @GetMapping
    public List<PetDTO> getPets() {
        return petService.getAll();
    }

    @GetMapping("/owner/{ownerId}")
    public List<PetDTO> getPetsByOwner(@PathVariable long ownerId) {
        return petService.getAllByOwner(ownerId);
    }
}
