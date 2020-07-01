package com.example.demo.controllers;

import com.example.demo.entities.Project;
import com.example.demo.repositories.ProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin(origins = "https://localhost:4200")
public class ProjectController {
    @Autowired
    private ProjectRepository repository;

    //DEPARTMENT
    @GetMapping("/departments")
    public String[] getdepartments() {
        String[] dep = {"Telecom", "Financial Services", "Life Sciences", "Healthcare", "Product Engineering"};
        return dep;
    }

    //To Get All the Projects
    @GetMapping("/projects")
    public List<Project> getAllProject() {
        return repository.findAllProjects();
    }

    //Create a new Project
    @PostMapping("/projects")
    void createProject(@RequestBody Project p) {
        System.out.println(p.getProjectName() + ":");
        System.out.println("Project Goal: " + p.getProjectGoal());
        System.out.println("Start Date: " + p.getStartDate());
        System.out.println("End Date: " + p.getEndDate());
        System.out.println("Client Name: " + p.getClientName());
        System.out.println("Team Size: " + p.getTeamSize());
        System.out.println("Department: " + p.getDepartment());
        repository.save(p);
    }

    //Get Project by their ID
    @GetMapping("projects/{id}")
    public Project getProjectByID(@PathVariable("id") Long id){
        Optional<Project> proj = repository.findById(id);
        if(proj.isPresent()){
            Project temp = proj.get();
            return temp;
        }
        return null;
    }

    //Update Project
    @PutMapping("project/{id}")
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

            repository.save(toUpdate);
        }
    }

}
