package com.udacity.critter.repository;

import com.udacity.critter.data.Employee;
import com.udacity.critter.data.Pet;
import com.udacity.critter.data.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface ScheduleRepository extends JpaRepository<Schedule, Long> {

    @Query("select s from Schedule as s where :employee member of s.employees")
    List<Schedule> queryFindAllByEmployee(Employee employee);

    List<Schedule> findByPetsContains(Pet pet);

    @SuppressWarnings("SpringDataRepositoryMethodParametersInspection")
    List<Schedule> findByPetsIn(List<Pet> pets);
}
