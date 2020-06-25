package com.example.demo.entities;

import com.example.demo.Department;

import javax.persistence.*;
import java.sql.Time;
import java.util.Date;

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
    private Date startDate;
    @Column(name = "endDate")
    private Date endDate;
    @Column(name = "active")
    private boolean active;

    // location and logistics
    @Column(name = "startWork")
    private Time startWork;
    @Column(name = "endWork")
    private Time endWork;
    @Column(name = "location")
    private String location;
    @Column(name = "timezone")
    private Timezone timezone;

    // current position profile
    @Column(name = "department")
    private Department department;
    @Column(name = "role")
    private String role;
    @Column(name = "manager")
    private String manager;


    public Employee() {
    }

    public Employee(String firstName, String lastName, String email, Date startDate, Timezone timezone, String role) {
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
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
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

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public Time getStartWork() {
        return startWork;
    }

    public void setStartWork(Time startWork) {
        this.startWork = startWork;
    }

    public Time getEndWork() {
        return endWork;
    }

    public void setEndWork(Time endWork) {
        this.endWork = endWork;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Timezone getTimezone() {
        return timezone;
    }

    public void setTimezone(Timezone timezone) {
        this.timezone = timezone;
    }

    public Department getDepartment() {
        return department;
    }

    public void setDepartment(Department department) {
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
        this.manager = manager;
    }
}
