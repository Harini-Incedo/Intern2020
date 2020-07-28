package com.example.demo.controllers;

import com.example.demo.entities.Engagement;
import com.example.demo.entities.Skill;
import com.example.demo.repositories.EmployeeRepository;
import com.example.demo.repositories.EngagementRepository;
import com.example.demo.repositories.SkillRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
public class SkillController {

    @Autowired
    private SkillRepository skillRepository;
    @Autowired
    private EngagementRepository engagementRepository;

    // INCOMPLETE
    public void addSkillToProject(long projID, String skillName, int totalWeeklyHours, int count) {
        Skill skillOnProject = skillRepository.findSkillOnProject(projID, skillName);
        if (skillOnProject == null) {
            skillOnProject = createSkill(skillName, totalWeeklyHours, projID);
        } else {
            updateSkillByID(skillOnProject, totalWeeklyHours);
        }
        for (int i = 0; i < count; i++) {
//            Engagement newEngagement = new Engagement(1l, projID, skillOnProject.getId(), );
//            engagementRepository.save(newEngagement);
        }
    }

    // creates a new skill on this project and returns it
    private Skill createSkill(String skillName, int totalWeeklyHours, long projectID) {
        Skill newSkill = new Skill(skillName, totalWeeklyHours, projectID);
        skillRepository.save(newSkill);
        return newSkill;
    }

    // updates a given skill by ID
    private void updateSkillByID(Skill skillToUpdate, int newHours) {
        skillToUpdate.setTotalWeeklyHours(newHours);
        skillRepository.save(skillToUpdate);
    }

}
