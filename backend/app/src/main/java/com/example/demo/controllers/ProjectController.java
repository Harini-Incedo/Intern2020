package com.example.demo.controllers;

import com.example.demo.entities.Project;
import com.example.demo.repositories.ProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
public class ProjectController {

    @Autowired
    private ProjectRepository repository;

    // To get four status options
    @GetMapping("/status")
    public String[] getStatus() {
        String[] status = {"Opened", "In Progress", "Completed", "Closed"};
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
        // Add validation for project status based on start date //
        repository.save(p);
    }

    // Returns the active project with the given ID, if it exists
    @GetMapping("projects/{id}")
    public Project getProjectByID(@PathVariable("id") Long id){
        Optional<Project> proj = repository.findById(id);
        if (proj.isPresent()){
            Project temp = proj.get();
            return temp;
        }
        return null;
    }

    // Updates the project status to be completed
    @PutMapping("projects/complete/{id}")
    void completeProjectByID(@PathVariable("id") Long id) {
        Project toComplete = getProjectByID(id);
        toComplete.setStatus("Completed");
        repository.save(toComplete);
    }

    // Updates the project status to be closed
    @PutMapping("projects/close/{id}")
    void closeProjectByID(@PathVariable("id") Long id) {
        Project toClose = getProjectByID(id);
        toClose.setStatus("Closed");
        repository.save(toClose);
    }

    // Updates the project with the given ID if it exists
    @PutMapping("projects/{id}")
    void updateProjectByID(@PathVariable("id")Long id, @RequestBody Project p){
        Project toUpdate = getProjectByID(id);
        if(toUpdate != null){

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
    }

}
