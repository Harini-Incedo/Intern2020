package com.example.demo.entities;

import com.fasterxml.jackson.annotation.JsonFormat;

import javax.persistence.*;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

@Entity
public class Engagement {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column(name = "employeeID")
    private long employeeID;
    @Column(name = "projectID")
    private long projectID;
    @Column(name = "skill")
    private long skillID;

    @Column(name = "startDate")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate startDate;
    @Column(name = "endDate")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate endDate;
    @Column(name = "hoursNeeded")
    private int hoursNeeded;

    // NEW //
    @ElementCollection
    @Column(name ="assignedWeeklyHours")
    private Map<LocalDate, Integer> assignedWeeklyHours;

    public Engagement() {

    }

    public Engagement(long employeeID, long projectID, long skillID, LocalDate startDate, LocalDate endDate, int hoursNeeded) {
        this.employeeID = employeeID;
        this.projectID =  projectID;
        this.skillID = skillID;
        this.startDate = startDate;
        this.endDate = endDate;
        this.hoursNeeded = hoursNeeded;
        System.out.println(startDate + " " + endDate);
        this.assignedWeeklyHours = defaultWeeklyHoursMapping();
    }

    // helper method:
    // creates "empty" week to hours mapping based on start
    // and end date for this engagement.
    public Map<LocalDate, Integer> defaultWeeklyHoursMapping() {
        Map<LocalDate, Integer> toReturn = new HashMap<>();
        LocalDate mondayOfStartingWeek = this.startDate.with(DayOfWeek.MONDAY);
        LocalDate temp = mondayOfStartingWeek;
        while (temp.isBefore(this.endDate)) {
            toReturn.put(temp, 0);
            temp = temp.plusWeeks(1);
        }
        return toReturn;
    }

    public long getId() {
        return id;
    }

    public long getEmployeeID() {
        return employeeID;
    }

    public void setEmployeeID(long employeeID) {
        this.employeeID = employeeID;
    }

    public long getProjectID() {
        return projectID;
    }

    public void setProjectID(long projectID) {
        this.projectID = projectID;
    }

    public long getSkillID() {
        return skillID;
    }

    public void setSkillID(long skillID) {
        this.skillID = skillID;
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

    public Map<LocalDate, Integer> getAssignedWeeklyHours() {
        return assignedWeeklyHours;
    }

    public void setAssignedWeeklyHours(Map<LocalDate, Integer> assignedWeeklyHours) {
        this.assignedWeeklyHours = assignedWeeklyHours;
    }

    public int getHoursNeeded() {
        return hoursNeeded;
    }

    public void setHoursNeeded(int hoursNeeded) {
        this.hoursNeeded = hoursNeeded;
    }

    @Override
    public String toString() {
        return "Engagement{" +
                "id=" + id +
                ", employeeID=" + employeeID +
                ", projectID=" + projectID +
                ", skillID=" + skillID +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                ", hoursNeeded=" + hoursNeeded +
                ", assignedWeeklyHours=" + assignedWeeklyHours +
                '}';
    }
}
