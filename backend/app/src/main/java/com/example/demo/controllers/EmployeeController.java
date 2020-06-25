package com.example.demo.controllers;

import com.example.demo.entities.Employee;
import com.example.demo.repositories.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
public class EmployeeController {

    @Autowired
    private EmployeeRepository repository;


    @GetMapping("/employees")
    public List<Employee> getAllEmployees() {
        return repository.findAllActive();
    }


    @GetMapping("employees/{id}")
    public Employee getEmployeeByID(@PathVariable("id") Long id) {
        Optional<Employee> employee = repository.findById(id);
        if (employee.isPresent()) {
            Employee toReturn = employee.get();
            // if there exists an active employee with the given ID
            if (toReturn.isActive()) {
                return toReturn;
            }
        }
        return null;
    }


    @PostMapping("/employees")
    void createEmployee(@RequestBody Employee e) {
        e.setActive(true);
        repository.save(e);
    }


    @DeleteMapping("employees/{id}")
    void deleteEmployeeByID(@PathVariable("id") Long id) {
        Employee toDelete = getEmployeeByID(id);
        if (toDelete != null) {
            toDelete.setActive(false);
            repository.save(toDelete);
        }
    }

}
