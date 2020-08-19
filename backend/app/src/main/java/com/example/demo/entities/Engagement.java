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

    @ElementCollection
    @Column(name ="assignedWeeklyHours")
    private Map<LocalDate, Integer> assignedWeeklyHours;
    @Column(name = "avgWeeklyEngHours")
    private int avgWeeklyEngHours;
    @Column(name ="billable")
    private boolean billable;

    public Engagement() {

    }

  public Engagement(long projectID, long skillID, LocalDate startDate, LocalDate endDate, int avgWeeklyEngHours, boolean billable) {
        this.projectID =  projectID;
        this.skillID = skillID;
        this.startDate = startDate;
        this.endDate = endDate;
        this.billable = billable;
        this.avgWeeklyEngHours = avgWeeklyEngHours;
        this.assignedWeeklyHours = defaultWeeklyHoursMapping(avgWeeklyEngHours);
    }

    // helper method:
    // creates "empty" week to hours mapping based on start
    // and end date for this engagement.
    private Map<LocalDate, Integer> defaultWeeklyHoursMapping(int avgHours) {
        Map<LocalDate, Integer> toReturn = new HashMap<>();
        LocalDate mondayOfStartingWeek = this.startDate.with(DayOfWeek.MONDAY);
        LocalDate temp = mondayOfStartingWeek;
        while (temp.isBefore(this.endDate)) {
            toReturn.put(temp, avgHours);
            temp = temp.plusWeeks(1);
        }
        return toReturn;
    }

    // helper method:
    // updates hourly mapping of engagement when the timeline is changed
    public void extendMappings(LocalDate newStartDate, LocalDate newEndDate) {
        LocalDate mondayOfStartingWeek = newStartDate.with(DayOfWeek.MONDAY);
        LocalDate temp = mondayOfStartingWeek;
        while (temp.isBefore(newEndDate)) {
            if (!assignedWeeklyHours.containsKey(temp)) {
                assignedWeeklyHours.put(temp, avgWeeklyEngHours);
            }
            temp = temp.plusWeeks(1);
        }
        setStartDate(newStartDate);
        setEndDate(newEndDate);
    }

    // helper method:
    // updates the hourly mappings for the weeks, starting with startDate
    // and up to count # of weeks after startDate, with the given new hours
    public void massUpdateHours(LocalDate startDate, LocalDate endDate, int newHours, LocalDate projectStartDate) {
        LocalDate mondayOfStartingWeek = startDate.with(DayOfWeek.MONDAY);
        LocalDate temp = checkForProjectStartDate(mondayOfStartingWeek, projectStartDate);
        while (temp.isBefore(endDate)) {
            assignedWeeklyHours.replace(temp, newHours);
            temp = temp.plusWeeks(1);
        }
    }

    private LocalDate checkForProjectStartDate(LocalDate temp, LocalDate projectStartDate) {
        if (temp.isBefore(projectStartDate)) {
            temp = temp.plusWeeks(1);
        }
        return temp;
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

    public boolean getBillable() {
        return billable;
    }

    public void setBillable(boolean billable) {
        this.billable = billable;
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
                ", assignedWeeklyHours=" + assignedWeeklyHours +
                '}';
    }
}
