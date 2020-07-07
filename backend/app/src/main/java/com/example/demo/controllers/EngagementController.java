package com.example.demo.controllers;

import com.example.demo.entities.Engagement;
import com.example.demo.repositories.EngagementRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
public class EngagementController {

    @Autowired
    private EngagementRepository repository;

    // To get all engagements associated with the given project
    @GetMapping("projects/{id}/engagements")
    public List<Engagement> getEngagementsByProjectID(@PathVariable("id") Long id) {
        return repository.findEngagementsByProjectID(id);
    }

    // To get all engagements associated with the given employee
    @GetMapping("employees/{id}/engagements")
    public List<Engagement> getEngagementsByEmployeeID(@PathVariable("id") Long id) {
        return repository.findEngagementsByEmployeeID(id);
    }

    // Creates a new engagement in the database with the given information
    @PostMapping("/engagements")
    void createProject(@RequestBody Engagement e) {
        repository.save(e);
    }

    // Returns the engagement with the given ID, if it exists
    @GetMapping("engagements/{id}")
    private Engagement getEngagementByID(@PathVariable("id") Long id){
        Optional<Engagement> engagement = repository.findById(id);
        if (engagement.isPresent()){
            Engagement temp = engagement.get();
            return temp;
        }
        return null;
    }

    // Updates the engagement with the given ID if it exists
    @PutMapping("engagements/{id}")
    void updateEngagementByID(@PathVariable("id")Long id, @RequestBody Engagement e){
        Engagement toUpdate = getEngagementByID(id);
        if (toUpdate != null){

            toUpdate.setEmployeeID(e.getEmployeeID());
            toUpdate.setProjectID(e.getProjectID());
            toUpdate.setStartDate(e.getStartDate());
            toUpdate.setEndDate(e.getEndDate());
            toUpdate.setRole(e.getRole());
            toUpdate.setHoursNeeded(e.getHoursNeeded());

            repository.save(toUpdate);
        }
    }

}
