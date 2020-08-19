package com.example.demo.controllers;

import com.example.demo.entities.Engagement;
import com.example.demo.entities.Project;
import com.example.demo.entities.Skill;
import com.example.demo.repositories.EngagementRepository;
import com.example.demo.repositories.ProjectRepository;
import com.example.demo.repositories.SkillRepository;
import com.example.demo.validation.EntityNotFoundException;
import com.example.demo.validation.InvalidInputException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
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


    @PostMapping("/projects/{projID}/skills")
    public void addSkillToProject(@PathVariable("projID") long projID, @RequestBody HashMap<String, String> values) {

        // extracts necessary values from request body sent by UI
        System.out.println(values);
        String skillName = values.get("skillName");
        int avgWeeklySkillHours = Integer.parseInt(values.get("avgWeeklySkillHours"));
        int avgWeeklyEngHours = Integer.parseInt(values.get("avgWeeklyEngHours"));
        int count = Integer.parseInt(values.get("count"));

        // creates a new skill object on project if one does not exist.
        // otherwise, throws error informing user of the pre-existing skill.
        Skill skillOnProject = skillRepository.findSkillOnProject(projID, skillName);
        if (skillOnProject != null) {
            throw new InvalidInputException("The skill, " + skillName + ", already exists on this project.",
                    "Please add engagements/update hours on the existing skill.");
        }
        skillOnProject = createSkill(skillName, avgWeeklySkillHours, projID);

        // creates count many "empty" engagements with every detail filled
        // except the employee on the engagement.
        Project p = (projectRepository.findById(projID)).get();
        for (int i = 0; i < count; i++) {
            Engagement newEngagement = new Engagement(projID, skillOnProject.getId(),
                                                            p.getStartDate(), p.getEndDate(),
                                                                    avgWeeklyEngHours, false);
            engagementRepository.save(newEngagement);
        }

    }

    @PutMapping("/projects/{projID}/skills")
    public void addEngagementsToSkill(@PathVariable("projID") long projID, @RequestBody HashMap<String, String> values) {

        // extracts necessary values from request body sent by UI
        System.out.println(values);
        String skillName = values.get("skillName");
        int avgWeeklyEngHours = Integer.parseInt(values.get("avgWeeklyEngHours"));
        int count = Integer.parseInt(values.get("count"));

        // creates a new skill object on project if one does not exist.
        // otherwise, updates existing object with given hours.
        Skill skillOnProject = skillRepository.findSkillOnProject(projID, skillName);

        if (skillOnProject == null) {
            throw new EntityNotFoundException("No skill exists with this ID: " + skillOnProject.getId(),
                    "Please use a valid skill ID.");
        }

        // creates count many "empty" engagements with every detail filled
        // except the employee on the engagement.
        Project p = (projectRepository.findById(projID)).get();
        for (int i = 0; i < count; i++) {
            Engagement newEngagement = new Engagement(projID, skillOnProject.getId(),
                    p.getStartDate(), p.getEndDate(),
                    avgWeeklyEngHours, false);
            System.out.println(newEngagement);
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
        Project project = projectRepository.findById(projectID).get();
        Skill newSkill = new Skill(skillName, totalWeeklyHours, projectID,
                                            project.getStartDate(), project.getEndDate());
        skillRepository.save(newSkill);
        return newSkill;
    }


    // updates a given skill by ID
    @PutMapping("/skills/{id}/update")
    public void updateSkillByID(@PathVariable("id") Long id, @RequestBody HashMap<String, String> values)
                                                        throws EntityNotFoundException {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate startDate = LocalDate.parse(values.get("startDate"), dtf);
        LocalDate endDate = LocalDate.parse(values.get("endDate"), dtf);
        int newHours = Integer.parseInt(values.get("newHours"));

        Skill toUpdate = getSkillByID(id);

        Project p = projectRepository.findById(toUpdate.getProjectID()).get();

        /* Date Validation */
        if (endDate != null && startDate.isAfter(endDate)) {
            throw new InvalidInputException("End Date is invalid: " + endDate,
                    "End Date should be equal to or later than Start Date.");
        }

        toUpdate.massUpdateHours(startDate, endDate, newHours, p.getStartDate());
        skillRepository.save(toUpdate);
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
