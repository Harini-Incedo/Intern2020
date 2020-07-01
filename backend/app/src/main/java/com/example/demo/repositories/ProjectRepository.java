package com.example.demo.repositories;

import com.example.demo.entities.Project;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;

@Repository
public interface ProjectRepository extends CrudRepository <Project, Long> {
    @Query("SELECT * FROM Project ORDER BY projectName ")
    List<Project> findAllProjects();
}
