package com.example.demo.controllers;

import com.example.demo.entities.Engagement;
import com.example.demo.entities.Project;
import com.example.demo.entities.Skill;
import com.example.demo.repositories.EngagementRepository;
import com.example.demo.repositories.ProjectRepository;
import com.example.demo.repositories.SkillRepository;
import com.example.demo.validation.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;

@RestController
@CrossOrigin(origins = "*")
public class SkillController {

    @Autowired
    private SkillRepository skillRepository;
    @Autowired
    private EngagementRepository engagementRepository;
    @Autowired
    private ProjectRepository projectRepository;


    @PutMapping("/projects/{projID}/skills")
    public void addSkillToProject(@PathVariable("projID") long projID, @RequestBody HashMap<String, String> values) {

        // extracts necessary values from request body sent by UI
        System.out.println(values);
        String skillName = values.get("skillName");
        int totalWeeklyHours = Integer.parseInt(values.get("totalWeeklyHours"));
        int avgWeeklyEngHours = Integer.parseInt(values.get("avgWeeklyEngHours"));
        int count = Integer.parseInt(values.get("count"));

        // creates a new skill object on project if one does not exist.
        // otherwise, updates existing object with given hours.
        Skill skillOnProject = skillRepository.findSkillOnProject(projID, skillName);
        if (skillOnProject == null) {
            skillOnProject = createSkill(skillName, totalWeeklyHours, projID);
        } else {
            updateSkillHours(skillOnProject, totalWeeklyHours);
        }

        // creates count many "empty" engagements with every detail filled
        // except the employee on the engagement.
        for (int i = 0; i < count; i++) {
            Project p = (projectRepository.findById(projID)).get();
            Engagement newEngagement = new Engagement(projID, skillOnProject.getId(),
                                                            p.getStartDate(), p.getEndDate(),
                                                                    avgWeeklyEngHours, false);
            engagementRepository.save(newEngagement);
        }

    }

    // gets a given skill by ID
    @GetMapping("/skills/{id}")
    public Skill getSkillByID(@PathVariable("id") long id) {
        return (skillRepository.findById(id)).get();
    }

    // For testing purposes:
    // returns all skills in the skill database table
    @GetMapping("/databaseSkills")
    public List<Skill> getAllSkills() {
        return (List<Skill>) skillRepository.findAll();
    }

    // creates a new skill on this project and returns it
    private Skill createSkill(String skillName, int totalWeeklyHours, long projectID) {
        Skill newSkill = new Skill(skillName, totalWeeklyHours, projectID);
        skillRepository.save(newSkill);
        return newSkill;
    }

    // helper method:
    // updates a given skill with new hours
    private void updateSkillHours(Skill skillToUpdate, int newHours) {
        skillToUpdate.setTotalWeeklyHours(newHours);
        skillRepository.save(skillToUpdate);
    }

    // updates a given skill by ID
    @PutMapping("/skills/{id}/update")
    public void updateSkillByID(@PathVariable("id") Long id, @RequestBody int newHours)
                                                        throws EntityNotFoundException {
        Skill toUpdate = getSkillByID(id);
        updateSkillHours(toUpdate, newHours);
    }

    // deletes a given skill by ID
    @DeleteMapping("/skills/{id}")
    public void deleteSkillByID(@PathVariable("id") Long id) throws EntityNotFoundException {
        Skill toDelete = getSkillByID(id);
        // delete all engagements which have reference to this skillID
        List<Engagement> engagements = skillRepository.getEngagementsBySkillID(id);
        for (Engagement e : engagements) {
            engagementRepository.delete(e);
        }
        skillRepository.delete(toDelete);
    }

}
