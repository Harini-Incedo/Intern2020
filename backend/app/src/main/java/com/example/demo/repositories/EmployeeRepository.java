package com.example.demo.repositories;

import com.example.demo.entities.Employee;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EmployeeRepository extends CrudRepository<Employee,Long> {

    @Query("SELECT e FROM Employee e WHERE e.active=true ORDER BY e.lastName ASC")
    List<Employee> findAllActive();

}