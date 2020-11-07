package com.udacity.critter.repository;

import com.udacity.critter.data.Employee;
import com.udacity.critter.data.Pet;
import com.udacity.critter.data.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;


@Repository
public interface ScheduleRepository extends JpaRepository<Schedule, Long> {
    List<Schedule> findByEmployeesContains(Employee employee);

    List<Schedule> findByPetsContains(Pet pet);

    List<Schedule> findByPetsIn(Collection<List<Pet>> pets);
}
