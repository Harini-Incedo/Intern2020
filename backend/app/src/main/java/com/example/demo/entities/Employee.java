package com.example.demo.entities;

import com.fasterxml.jackson.annotation.JsonFormat;

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
    private String workingHours;
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

    public void setFirstName(String firstName) {
        if(firstName.matches("^[a-zA-Z ]*$")) {
            this.firstName = firstName;
        }
        //Should display error message here that name has numbers or special characters
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        if(lastName.matches("^[a-zA-Z ]*$")) {
            this.lastName = lastName;
        }
        //Should display error message here that name has numbers or special characters
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

    public void setStartDate(LocalDate startDate) {
        LocalDate min = LocalDate.parse("2012,01,01");
        if (startDate.compareTo(min) >= 0) {
            this.startDate = startDate;
        }
        //throw an error message that start date is less than min date
    }


    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        LocalDate temp = getStartDate();
        if (endDate.compareTo(temp) > 0) {
            this.endDate = endDate;
        }
        //throw error message that employee cant get fired in one day :'(
    }

    public String getWorkingHours() {
        return workingHours;
    }

    public void setWorkingHours(String workingHours) {
        if (workingHours.matches(("^[0-9]*$"))) {
            this.workingHours = workingHours;
        }
        //throw error since working hours is not a number
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        if(location.matches("^[a-zA-Z ]*$")) {
            this.location = location;
        }
        //throw error that Location is invalid.

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

    public void setManager(String manager) {
        if(manager.matches("^[a-zA-Z ]*$")) {
            this.manager = manager;
        }
        //throw error that manager is invalid I think we still have to chekc if he exists in employee

    }

}
