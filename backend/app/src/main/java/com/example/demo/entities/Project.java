package com.example.demo.entities;

import com.fasterxml.jackson.annotation.JsonFormat;
import jdk.vm.ci.meta.Local;

import javax.persistence.*;
import java.time.LocalDate;



@Entity
@Table(name = "project")
public class Project {

    public enum Timezone{
        PST,EST,IST
    }
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    @Column(name = "projectName")
    private String  projectName;
    @Column(name = "projectGoal")
    private String projectGoal;
    @Column(name = "clientName")
    private String clientName;
    @Column(name = "startDate")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate startDate;
    @Column(name = "endDate")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate endDate;
    @Column(name = "department")
    private String department;
    @Column(name ="teamSize")
    private String teamSize;

    public Project(String projectName, String clientName, LocalDate startDate){
        this.projectName = projectName;
        this.clientName =  clientName;
        this.startDate = startDate;
    }


    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getProjectGoal() {
        return projectGoal;
    }

    public void setProjectGoal(String projectGoal) {
        this.projectGoal = projectGoal;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getTeamSize() {
        return teamSize;
    }

    public void setTeamSize(String teamSize) {
        this.teamSize = teamSize;
    }



}

