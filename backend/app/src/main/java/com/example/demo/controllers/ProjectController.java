package com.example.demo.controllers;

import com.example.demo.entities.Project;
import com.example.demo.entities.Skill;
import com.example.demo.repositories.ProjectRepository;
import com.example.demo.repositories.SkillRepository;
import com.example.demo.validation.EntityNotFoundException;
import com.example.demo.validation.InvalidInputException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.*;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
public class ProjectController {
    @Autowired
    private ProjectRepository repository;
    private SkillRepository skillRepository;
    private final String[] status = {"Pending", "In Progress", "Completed", "Closed"};

    // To get project status options
    @GetMapping("/status")
    public String[] getStatus() {
        return status;
    }

    // To get all active projects in sorted order by client name
    @GetMapping("/projects")
    public List<Project> getAllProjects() {
        return repository.findAllProjects();
    }

    // Creates a new project in the database with the given information
    @PostMapping("/projects")
    void createProject(@RequestBody Project p) {
        System.out.println(p.getProjectName() + ":");
        System.out.println("Project Goal: " + p.getProjectGoal());
        System.out.println("Start Date: " + p.getStartDate());
        System.out.println("End Date: " + p.getEndDate());
        System.out.println("Client Name: " + p.getClientName());
        System.out.println("Team Size: " + p.getTeamSize());
        System.out.println("Department: " + p.getDepartment());

        // INPUT VALIDATION //
        validateProjectDetails(p);

        // Add validation for project status based on start date //
        p.setStatus("Pending");

        repository.save(p);
    }

    // Returns the active project with the given ID, if it exists
    @GetMapping("projects/{id}")
    public Project getProjectByID(@PathVariable("id") Long id) throws EntityNotFoundException {
        Optional<Project> proj = repository.findById(id);
        if (proj.isPresent()){
            Project temp = proj.get();
            return temp;
        }
        throw new EntityNotFoundException("No project exists with this ID: " + id,
                                                        "Please use a valid project ID.");
    }

    // Updates the project status to be completed
    @PutMapping("projects/complete/{id}")
    void completeProjectByID(@PathVariable("id") Long id) throws EntityNotFoundException {
        Project toComplete = getProjectByID(id);
        // valid project ID
        toComplete.setStatus("Completed");
        repository.save(toComplete);
    }

    // Updates the project status to be closed
    @PutMapping("projects/close/{id}")
    void closeProjectByID(@PathVariable("id") Long id) throws EntityNotFoundException {
        Project toClose = getProjectByID(id);
        // valid project ID
        toClose.setStatus("Closed");
        repository.save(toClose);
    }

    // Updates the project status to be in progress
    @PutMapping("projects/start/{id}")
    void startProjectByID(@PathVariable("id") Long id) throws EntityNotFoundException {
        Project toStart = getProjectByID(id);
        // valid project ID
        toStart.setStatus("In Progress");
        repository.save(toStart);
    }

    // Updates the project with the given ID if it exists
    @PutMapping("projects/{id}")
    void updateProjectByID(@PathVariable("id")Long id, @RequestBody Project p) {
        Project toUpdate = getProjectByID(id);

        // INPUT VALIDATION //
        validateProjectDetails(p);

        toUpdate.setProjectName(p.getProjectName());
        toUpdate.setProjectGoal(p.getProjectGoal());
        toUpdate.setStartDate(p.getStartDate());
        toUpdate.setEndDate(p.getEndDate());
        toUpdate.setClientName(p.getClientName());
        toUpdate.setTeamSize(p.getTeamSize());
        toUpdate.setDepartment(p.getDepartment());
        toUpdate.setWeeklyHours(p.getWeeklyHours());

        repository.save(toUpdate);
    }

    // Validates project details input by the user
    private void validateProjectDetails(Project p) throws InvalidInputException {
        /* Project Name */
        if (p.getProjectName() != null && !p.getProjectName().matches("[a-zA-Z0-9 -]*")) {
            throw new InvalidInputException("Invalid Project Name: " + p.getProjectName(),
                                                "Project name should not contain any special characters.");
        }
        /* Client Name */
        if (p.getClientName() != null && !p.getClientName().matches("[a-zA-Z0-9 -]*")) {
            throw new InvalidInputException("Invalid Client Name: " + p.getProjectName(),
                                                "Client name should not contain any special characters.");
        }
        /* Start Date */
        if (p.getStartDate() != null && !p.getStartDate().isAfter(LocalDate.of(2012, 1, 1))) {
            throw new InvalidInputException("Start Date is invalid: " + p.getStartDate(),
                                                "Start Date should be on or after January 1st, 2012.");
        }
        /* End Date */
        if (p.getEndDate() != null && !p.getEndDate().isAfter(p.getStartDate())) {
            throw new InvalidInputException("End Date is invalid: " + p.getEndDate(),
                                                "End Date should be equal to or later than Start Date.");
        }
        /* Weekly Hours */
        if (p.getWeeklyHours() < 0) {
            throw new InvalidInputException("Invalid Weekly Hours: " + p.getWeeklyHours(),
                                                "Weekly hours should be a positive integer value.");
        }
        /* Team Size */
        if (p.getTeamSize() < 0) {
            throw new InvalidInputException("Invalid Team Size: " + p.getTeamSize(),
                                                "Team size should be a positive integer value.");
        }
    }
    // Gets a list of Engagements by Project ID


}
