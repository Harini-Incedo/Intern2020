package com.example.demo.repositories;

import com.example.demo.entities.Employee;
import com.example.demo.entities.Engagement;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EmployeeRepository extends CrudRepository<Employee,Long> {

    @Query("SELECT e FROM Employee e WHERE e.active=true ORDER BY e.lastName ASC")
    List<Employee> findAllActive();

    @Query("SELECT e FROM Employee e WHERE e.active=false ORDER BY e.lastName ASC")
    List<Employee> findAllInactive();

    @Query("SELECT e FROM Employee e ORDER BY e.lastName ASC")
    List<Employee> findAll();

    @Query("SELECT e FROM Employee e WHERE e.email=?1")
    Employee checkEmailDuplication(String email);

    @Query("SELECT e FROM Engagement e WHERE e.employeeID=?1")
    List<Engagement> findEngagementsByEmployeeID(long id);

}
