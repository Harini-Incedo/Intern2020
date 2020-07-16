package com.example.demo.controllers;

import com.example.demo.entities.Employee;
import com.example.demo.entities.Project;
import com.example.demo.repositories.EmployeeRepository;
import com.example.demo.validation.EntityNotFoundException;
import com.example.demo.validation.InvalidInputException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
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
    public Employee getEmployeeByID(@PathVariable("id") Long id) throws EntityNotFoundException {
        Optional<Employee> employee = repository.findById(id);
        if (employee.isPresent()) {
            Employee toReturn = employee.get();
            // if there exists an active employee with the given ID
            if (toReturn.isActive()) {
                return toReturn;
            }
            //error message that status is not active

        }

            throw new EntityNotFoundException("No Employee exists with this ID: " + id, "Please use a valid ID.");

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
        vaildEmployeeDetails(e);
        repository.save(e);
    }

    @DeleteMapping("employees/{id}")
    void deleteEmployeeByID(@PathVariable("id") Long id) throws EntityNotFoundException {
        Employee toDelete = getEmployeeByID(id);
        if (toDelete != null) {
            if(toDelete.isActive()== true){
                toDelete.setActive(false);
                repository.save(toDelete);
            }

        }
        else{
            invalidEmployeeID(id);
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
        else{
            invalidEmployeeID(id);
        }
    }

    private void invalidEmployeeID(long id) throws EntityNotFoundException {
        throw new EntityNotFoundException("No Employee exists with this ID: " + id, "Please use a valid ID.");
    }
    private void vaildEmployeeDetails(Employee e) throws InvalidInputException {

        if (e.getFirstName() == null || !e.getFirstName().matches("^[a-zA-Z ]*$")) {
            throw new InvalidInputException("Invalid First Name: " + e.getFirstName(),
                    "First Name should not contain any special characters.");
        }

        if (e.getLastName() == null || !e.getLastName().matches("^[a-zA-Z ]*$")) {
            throw new InvalidInputException("Invalid Last Name: " + e.getLastName(),
                    "Last Name should not contain any special characters.");
        }

        if (e.getStartDate() == null || !e.getStartDate().isAfter(LocalDate.of(2012, 1, 1))) {
            throw new InvalidInputException("Start Date is invalid: " + e.getStartDate(),
                    "Start Date should be on or after January 1st, 2012.");
        }

        if (e.getEndDate() == null || !e.getEndDate().isAfter(e.getStartDate())) {
            throw new InvalidInputException("End Date is invalid: " + e.getEndDate(),
                    "End Date should be equal to or later than Start Date.");
        }

        if (e.getWorkingHours() < 0) {
            throw new InvalidInputException("Invalid Weekly Hours: " + e.getWorkingHours(),
                    "Weekly hours should be a positive integer value.");
        }

        if (e.getEmail() == null ) {
            throw new InvalidInputException("Invalid Email: " + e.getEmail(),
                    "Please input an EmailID");
        }
        if(e.getDepartment() == null){
            throw new InvalidInputException("Invalid Department: " + e.getDepartment(),"Please Input a Department");
        }
        if (e.getRole() == null){
            throw new InvalidInputException("Invalid Role: " + e.getRole(),"Please Input a Role");
        }
        if(e.getLocation() == null || !e.getLocation().matches("^[a-zA-Z ]*$")){
            throw new InvalidInputException("Invalid Location: " + e.getLocation(), "Location should not have any special characters");
        }
        if(e.getTimezone() == null){
            throw new InvalidInputException("Invalid Timezone: " + e.getTimezone(),"Please Input a Timezone");
        }
        if(e.getManager() == null || !e.getManager().matches("^[a-zA-Z ]*$"))
        {
            throw new InvalidInputException("Invalid Manager: " + e.getManager(), "Manager should not have any special characters");
        }
    }

}
