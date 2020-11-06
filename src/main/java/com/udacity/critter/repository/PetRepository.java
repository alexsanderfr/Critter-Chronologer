package com.udacity.critter.repository;

import com.udacity.critter.data.Customer;
import com.udacity.critter.data.Pet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PetRepository extends JpaRepository<Pet, Long> {
    List<Pet> findByOwner(Customer customer);
}
