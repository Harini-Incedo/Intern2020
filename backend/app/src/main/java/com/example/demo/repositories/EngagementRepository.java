package com.example.demo.repositories;

import com.example.demo.entities.Employee;
import com.example.demo.entities.Engagement;
import com.example.demo.entities.Project;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EngagementRepository extends CrudRepository<Engagement,Long> {

    @Query("SELECT e FROM Engagement e WHERE e.projectID=?1")
    List<Engagement> findEngagementsByProjectID(long id);

    @Query("SELECT e FROM Engagement e WHERE e.employeeID=?1")
    List<Engagement> findEngagementsByEmployeeID(long id);

    @Query("SELECT e FROM Employee e WHERE e.id=?1")
    Employee findEmployeeByID(long id);

    @Query("SELECT p FROM Project p WHERE p.id=?1")
    Project findProjectByID(long id);

}
