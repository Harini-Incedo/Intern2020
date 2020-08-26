package com.example.demo.entities;

import com.fasterxml.jackson.annotation.JsonFormat;
import jdk.vm.ci.meta.Local;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.awt.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "project")
public class Project {

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
    private int teamSize;
    @Column(name ="totalPlannedHours")
    private int totalPlannedHours;
    @Column(name ="totalAllocatedHours")
    private int totalAllocatedHours;
    @Column(name = "status")
    private String status;


    public Project(String projectName, String clientName, LocalDate startDate, String status) {
        this.projectName = projectName;
        this.clientName =  clientName;
        this.startDate = startDate;
        this.status = status;
    }

    public Project() {
        super();
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

    public int getTeamSize() {
        return teamSize;
    }

    public void setTeamSize(int teamSize) {
        this.teamSize = teamSize;
    }

    public int getTotalPlannedHours() { return totalPlannedHours; }

    public void setTotalPlannedHours(int totalPlannedHours) { this.totalPlannedHours = totalPlannedHours; }

    public int getTotalAllocatedHours() { return totalAllocatedHours; }

    public void setTotalAllocatedHours(int totalAllocatedHours) { this.totalAllocatedHours = totalAllocatedHours; }

    public long getId() {
        return id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Project{" +
                "id=" + id +
                ", projectName='" + projectName + '\'' +
                ", projectGoal='" + projectGoal + '\'' +
                ", clientName='" + clientName + '\'' +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                ", department='" + department + '\'' +
                ", teamSize=" + teamSize +
                ", totalPlannedHours=" + totalPlannedHours +
                ", totalAllocatedHours=" + totalAllocatedHours +
                ", status='" + status + '\'' +
                '}';
    }
}

