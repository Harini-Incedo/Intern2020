package com.example.demo.controllers;

import com.example.demo.entities.Employee;
import com.example.demo.entities.Engagement;
import com.example.demo.repositories.EmployeeRepository;
import com.example.demo.validation.EntityNotFoundException;
import com.example.demo.validation.InvalidInputException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
public class EmployeeController {

    @Autowired
    private EmployeeRepository repository;
    private final String[] roles = {"Analyst", "Developer", "Consultant", "Data Scientist",
            "Intern", "Manager", "HR", "Assistant"};
    private final String[] departments = {"Telecom", "Financial Services", "Life Sciences",
            "Healthcare", "Product Engineering"};
    private final String[] skills = { "Java","Python","C","C++","UI","SQL","Cloud Computing",
                                        "Ruby","R","Data Science","Machine Learning","Go","Finance",
                                            "Marketing","Human Resource","Management"};

    // To get all active employees in sorted order by last name
    @GetMapping("/employees/Active")
    public List<Employee> getAllActiveEmployees() {
        return repository.findAllActive();
    }

    // To get all inactive employees in sorted order by last name
    @GetMapping("/employees/Inactive")
    public List<Employee> getAllInactiveEmployees() {
        return repository.findAllInactive();
    }

    // To get all employees in sorted order by last name
    @GetMapping("/employees/Both")
    public List<Employee> getAllEmployees() {
        return repository.findAll();
    }

    // To get all recommended employees in sorted order by last name
    @GetMapping("/employees/recommended")
    public List<Employee> getRecommendedEmployees(@RequestBody HashMap<String, String> values) {
        // extracts necessary values from request body sent by UI
        String skill = values.get("skill");
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate startDate = LocalDate.parse(values.get("startDate"), dtf);
        LocalDate endDate = LocalDate.parse(values.get("endDate"), dtf);

        List<Employee> active = repository.findAllActive();
        List<Employee> employeesWithSkill = new ArrayList<>();
        // iterates over all active employees and extracts only those who
        // have the desired skill
        for (Employee e : active) {
//            if (containsSkill(e.getSkills(), skill)) {
//                employeesWithSkill.add(e);
//            }
            if (e.getSkills().contains(skill)) {
                employeesWithSkill.add(e);
            }
        }

        List<Employee> toReturn = new ArrayList<>();
        // filtering by availability
        for (Employee e : employeesWithSkill) {
            // employee should either not have an end date, or their end date
            // with the company should be after the end date of needed engagement.
            if (e.getEndDate() == null || e.getEndDate().isAfter(endDate)) {
                List<Engagement> engagements = repository.findEngagementsByEmployeeID(e.getId());
                // iterated over employees with desired skill, and extracts only those
                // who don't have engagements overlapping with needed engagement.
                boolean recommended = true;
                for (Engagement eng : engagements) {
                    if (containsOverlap(eng, startDate, endDate)) {
                        recommended = false;
                        break;
                    }
                }
                if (recommended) {
                    toReturn.add(e);
                }
            }
        }
        return toReturn;
    }

    // helper method:
    // returns true if the given engagement overlaps with the
    // duration between the given start and end date
    private boolean containsOverlap(Engagement e, LocalDate startDate, LocalDate endDate) {
        return !(startDate.isAfter(e.getEndDate()) || endDate.isBefore(e.getStartDate()));
    }

    // helper method:
    // returns true if given skill set contains given skill
    private boolean containsSkill(String[] skills, String skill) {
        for (String s : skills) {
            if (s.equals(skill)) {
                return true;
            }
        }
        return false;
    }

    // To get a preset list of roles
    @GetMapping("/roles")
    public String[] getRoles() {
        return roles;
    }

    // To get a preset list of skills
    @GetMapping("/skills")
    public String[] getSkills(){
        return skills;
    }

    // To get a preset list of departments
    @GetMapping("/departments")
    public String[] getDepartments() {
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
        }
        throw new EntityNotFoundException("No Employee exists with this ID: " + id,
                                                "Please use a valid employee ID.");
    }

    // Creates a new employee in the database with the given information
    @PostMapping("/employees")
    void createEmployee(@RequestBody Employee e) throws InvalidInputException {
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
        System.out.println("Skills : " + e.getSkills());
        // INPUT VALIDATION //
        validateEmployeeDetails(e);

        repository.save(e);
    }

    // Deletes the employee with the given ID, if it exists/is currently active
    @DeleteMapping("employees/{id}")
    void deleteEmployeeByID(@PathVariable("id") Long id) throws EntityNotFoundException {
        Employee toDelete = getEmployeeByID(id);
        toDelete.setActive(false);
        repository.save(toDelete);
    }

    // Updates the employee with the given ID if it exists
    @PutMapping("employees/{id}")
    void updateEmployeeByID(@PathVariable("id") Long id, @RequestBody Employee e)
                                throws EntityNotFoundException, InvalidInputException {
        Employee toUpdate = getEmployeeByID(id);

        // INPUT VALIDATION //
        validateEmployeeDetails(e);

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
        toUpdate.setSkills(e.getSkills());

        repository.save(toUpdate);

    }

    private void validateEmployeeDetails(Employee e) throws InvalidInputException {
        /* First Name */
        if (e.getFirstName() == null || !e.getFirstName().matches("^[a-zA-Z ]*$")) {
            throw new InvalidInputException("Invalid First Name: " + e.getFirstName(),
                    "First Name should not contain any special characters.");
        }
        /* Last Name */
        if (e.getLastName() == null || !e.getLastName().matches("^[a-zA-Z ]*$")) {
            throw new InvalidInputException("Invalid Last Name: " + e.getLastName(),
                    "Last Name should not contain any special characters.");
        }
        /* Start Date */
        if (e.getStartDate() == null || !e.getStartDate().isAfter(LocalDate.of(2012, 1, 1))) {
            throw new InvalidInputException("Start Date is invalid: " + e.getStartDate(),
                    "Start Date should be on or after January 1st, 2012.");
        }
        /* End Date */
        if (e.getEndDate() == null || !e.getEndDate().isAfter(e.getStartDate())) {
            throw new InvalidInputException("End Date is invalid: " + e.getEndDate(),
                    "End Date should be equal to or later than Start Date.");
        }
        /* Email */
        if (e.getEmail() == null) {
            throw new InvalidInputException("Invalid Email: " + e.getEmail(),
                    "Please input an EmailID");
        }
        /* Department */
        if (e.getDepartment() == null) {
            throw new InvalidInputException("No department selected.","Please select a department.");
        }
        /* Role */
        if (e.getRole() == null) {
            throw new InvalidInputException("No role selected.","Please select a role.");
        }
        /* Skills */
        if (e.getSkills() == null) {
            throw new InvalidInputException("Invalid Skills: " + e.getSkills(), "Invalid Skills");
        }
        /* Location */
        if (e.getLocation() != null && !e.getLocation().matches("^[a-zA-Z ]*$")) {
            throw new InvalidInputException("Invalid Location: " + e.getLocation(),
                                            "Location should contain have any special characters");
        }
        /* Time Zone */
        if (e.getTimezone() == null) {
            throw new InvalidInputException("No time zone selected.", "Please select a time zone.");
        }
        /* Manager Name */
        if (e.getManager() == null || !e.getManager().matches("^[a-zA-Z ]*$")) {
            throw new InvalidInputException("Invalid Manager Name: " + e.getManager(),
                                                "Manager name should not contain any special characters.");
        }
    }

}
