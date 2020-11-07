package com.udacity.critter.repository;

import com.udacity.critter.data.Customer;
import com.udacity.critter.data.Pet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {
    Customer findByPetsContains(Pet pet);
}
