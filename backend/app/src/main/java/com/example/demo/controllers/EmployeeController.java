package com.example.demo.controllers;

import com.example.demo.entities.Employee;
import com.example.demo.repositories.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
public class EmployeeController {

    @Autowired
    private EmployeeRepository repository;

    @GetMapping("/employees")
    public List<Employee> getAllEmployees() {
        return (List<Employee>) repository.findAll();
    }

//    @PostMapping("/employees")
//    void addUser(@RequestBody Employee e) {
//        repository.save(e);
//    }

}
