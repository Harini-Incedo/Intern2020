package com.example.demo.repositories;

import com.example.demo.entities.Engagement;
import com.example.demo.entities.Skill;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SkillRepository extends CrudRepository <Skill, Long> {

    @Query("SELECT s FROM Skill s WHERE s.projectID=?1")
    List<Skill> getSkillByProjectID(long projID);

    @Query("SELECT s FROM Skill s WHERE s.projectID=?1 AND s.skillName=?2")
    Skill findSkillOnProject(long projID, String skill);

    @Query("SELECT e FROM Engagement e WHERE e.skillID=?1")
    List<Engagement> getEngagementsBySkillID(long id);

}
