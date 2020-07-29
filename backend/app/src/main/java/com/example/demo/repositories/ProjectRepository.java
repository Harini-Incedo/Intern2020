package com.example.demo.repositories;

import com.example.demo.controllers.EngagementController;
import com.example.demo.entities.Engagement;
import com.example.demo.entities.Project;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;

@Repository
public interface ProjectRepository extends CrudRepository <Project, Long> {

    @Query("SELECT p FROM Project p ORDER BY p.clientName")
    List<Project> findAllProjects();

    @Query("SELECT e FROM Engagement e WHERE e.projectID=?1 AND e.skillID=?2")
    List<Engagement> getEngagementBySkill(long projID, long skillID);

}
