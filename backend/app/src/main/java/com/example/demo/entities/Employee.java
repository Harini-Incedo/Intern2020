package com.example.demo.entities;

import com.example.demo.Department;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Date;

@Entity
public class Employee {

    enum Timezone {
        PST,
        EST,
        IST;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    String firstName;
    String lastName;
    String email;
    Date startDate;
    Date endDate;
    String location;
    Enum<Timezone> timezone;
    Enum<Department> department;
    String role;
    String manager;


    public Employee() {
    }

    public Employee(String firstName, String lastName, String email, Date startDate, Enum<Timezone> timezone, String role) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.startDate = startDate;
        this.timezone = timezone;
        this.role = role;
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

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Enum<Timezone> getTimezone() {
        return timezone;
    }

    public void setTimezone(Enum<Timezone> timezone) {
        this.timezone = timezone;
    }

    public Enum<Department> getDepartment() {
        return department;
    }

    public void setDepartment(Enum<Department> department) {
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
