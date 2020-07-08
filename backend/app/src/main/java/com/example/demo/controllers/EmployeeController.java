package com.example.demo.controllers;

import com.example.demo.entities.Employee;
import com.example.demo.repositories.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
public class EmployeeController {

    @Autowired
    private EmployeeRepository repository;

    // To get all active employees in sorted order by last name
    @GetMapping("/employees")
    public List<Employee> getAllEmployees() {
        return repository.findAllActive();
    }

    // To get a preset list of roles
    @GetMapping("/roles")
    public String[] getRoles() {
        String[] roles = {"Analyst", "Developer", "Consultant", "Data Scientist",
                "Intern", "Manager", "HR", "Assistant"};
        return roles;
    }

    // To get a preset list of departments
    @GetMapping("/departments")
    public String[] getDepartments() {
        String[] departments = {"Telecom", "Financial Services", "Life Sciences",
                                        "Healthcare", "Product Engineering"};
        return departments;
    }

    // Returns the active employee with the given ID, if it exists
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

    // Creates a new employee in the database with the given information
    @PostMapping("/employees")
    void createEmployee(@RequestBody Employee e) {
        e.setActive(true);
        System.out.println(e.getFirstName() + " " + e.getLastName() + ":");
        System.out.println("Email: " + e.getEmail());
        System.out.println("Department: " + e.getDepartment());
        System.out.println("Manager: " + e.getManager());
        System.out.println("Start Date: " + e.getStartDate());
        System.out.println("End Date: " + e.getEndDate());
        System.out.println("Location: " + e.getLocation());
        System.out.println("Time Zone: " + e.getTimezone());
        System.out.println("Working Hours: " + e.getWorkingHours());
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

    // Updates the project with the given ID if it exists
    @PutMapping("employees/{id}")
    void updateEmployeeByID(@PathVariable("id") Long id, @RequestBody Employee e) {
        Employee toUpdate = getEmployeeByID(id);
        if (toUpdate != null) {

            toUpdate.setFirstName(e.getFirstName());
            toUpdate.setLastName(e.getLastName());
            toUpdate.setEmail(e.getEmail());

            toUpdate.setLocation(e.getLocation());
            toUpdate.setStartDate(e.getStartDate());
            toUpdate.setEndDate(e.getEndDate());

            toUpdate.setWorkingHours(e.getWorkingHours());
            toUpdate.setLocation(e.getLocation());
            toUpdate.setTimezone(e.getTimezone());

            toUpdate.setDepartment(e.getDepartment());
            toUpdate.setRole(e.getRole());
            toUpdate.setManager(e.getManager());

            repository.save(toUpdate);

        }
    }

}
