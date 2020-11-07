package com.udacity.critter.repository;

import com.udacity.critter.data.Employee;
import com.udacity.critter.enums.EmployeeSkill;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Set;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {
    List<Employee> findAllBySkillsIn(Collection<Set<EmployeeSkill>> skills);
}
