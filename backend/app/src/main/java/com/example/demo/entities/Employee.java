package com.example.demo.entities;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.example.demo.validation.EntityNotFoundException;
import com.example.demo.validation.InvalidInputException;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "employee")
public class Employee {

    public enum Timezone {
        PST,
        EST,
        IST
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    // contact information
    @Column(name = "firstName")
    private String firstName;
    @Column(name = "lastName")
    private String lastName;
    @Column(name = "email")
    private String email;

    // association with company
    @Column(name = "startDate")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate startDate;
    @Column(name = "endDate")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate endDate;
    @Column(name = "active")
    private boolean active;

    // location and logistics
    @Column(name = "workingHours")
    private int workingHours;
    @Column(name = "location")
    private String location;
    @Column(name = "timezone")
    private Timezone timezone;

    // current position profile
    @Column(name = "department")
    private String department;
    @Column(name = "role")
    private String role;
    @Column(name = "manager")
    private String manager;

    public Employee() {
    }

    public Employee(String firstName, String lastName, String email, LocalDate startDate, Timezone timezone, String role) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.startDate = startDate;
        this.timezone = timezone;
        this.role = role;
        this.active = true;
    }

    public long getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) throws InvalidInputException {
        if (firstName.matches("^[a-zA-Z ]*$")) {
            this.firstName = firstName;
        } else {
            throw new InvalidInputException("Invalid First Name: " + firstName,
                    "First name should not contain any special characters.");
        }
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) throws InvalidInputException{
        if(lastName.matches("^[a-zA-Z ]*$")) {
            this.lastName = lastName;
        }
        else {
            throw new InvalidInputException("Invalid Last Name: " + lastName,
                    "Last name should not contain any special characters.");
        }

    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) throws InvalidInputException{
        LocalDate min = LocalDate.parse("2012,01,01");
        if (startDate.compareTo(min) >= 0) {
            this.startDate = startDate;
        }
        else{
            throw new InvalidInputException("Start Date is invalid: " + startDate,
                    "Start Date should be on or after January 1st, 2012.");
        }
    }


    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate)throws InvalidInputException {
        LocalDate temp = getStartDate();
        if (endDate.compareTo(temp) > 0) {
            this.endDate = endDate;
        }
        throw new InvalidInputException("End Date is invalid: " + endDate,
                "End Date should be on or after January 1st, 2012.");
    }

    public int getWorkingHours() {
        return workingHours;
    }

    public void setWorkingHours(int workingHours) throws InvalidInputException{
        if (workingHours > 0) {
            this.workingHours = workingHours;
        }
        throw new InvalidInputException("Invalid Weekly Hours: " + workingHours,
                "Weekly hours should be a positive integer value.");
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) throws InvalidInputException{
        if(location.matches("^[a-zA-Z ]*$")) {
            this.location = location;
        }
        throw new InvalidInputException("Invalid Location: " + location,
                "Location should not be special characters.");

    }

    public Timezone getTimezone() {
        return timezone;
    }

    public void setTimezone(Timezone timezone) {
        this.timezone = timezone;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getManager() {
        return manager;
    }

    public void setManager(String manager) throws InvalidInputException {
        if(manager.matches("^[a-zA-Z ]*$")) {
            this.manager = manager;
        }
        throw new InvalidInputException("Invalid Manager: " + manager,
                "Manager should not have any special characters.");

    }

}
