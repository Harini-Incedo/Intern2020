package com.example.demo.entities;

import jdk.vm.ci.meta.Local;

import javax.persistence.*;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

@Entity
@Table(name = "skill")
public class Skill {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    @Column(name = "skillName")
    private String skillName;
    @Column(name = "avgWeeklyHours")
    private int avgWeeklyHours;
    @Column(name = "projectID")
    private long projectID;
    @ElementCollection
    @Column(name ="assignedWeeklyHours")
    private Map<LocalDate, Integer> assignedWeeklyHours;

    public Skill() {
        super();
    }

    public Skill(String skillName, int avgWeeklyHours, Long projectID, LocalDate startDate, LocalDate endDate) {
        this.skillName = skillName;
        this.avgWeeklyHours = avgWeeklyHours;
        this.projectID = projectID;
        this.assignedWeeklyHours = defaultWeeklyHoursMapping(avgWeeklyHours, startDate, endDate);
    }

    // helper method:
    // creates "empty" week-to-hours mapping based on the start
    // and end date of the project this skill is under.
    private Map<LocalDate, Integer> defaultWeeklyHoursMapping(int avgHours, LocalDate startDate, LocalDate endDate) {
        Map<LocalDate, Integer> toReturn = new HashMap<>();
        LocalDate mondayOfStartingWeek = startDate.with(DayOfWeek.MONDAY);
        LocalDate temp = mondayOfStartingWeek;
        while (temp.isBefore(endDate)) {
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
                assignedWeeklyHours.put(temp, avgWeeklyHours);
            }
            temp = temp.plusWeeks(1);
        }
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

    public String getSkillName() {
        return skillName;
    }

    public void setSkillName(String skillName) {
        this.skillName = skillName;
    }

    public int getAvgWeeklyHours() {
        return avgWeeklyHours;
    }

    public void setAvgWeeklyHours(int avgWeeklyHours) {
        this.avgWeeklyHours = avgWeeklyHours;
    }

    public void setProjectID(long projectID) {
        this.projectID = projectID;
    }

    public Map<LocalDate, Integer> getAssignedWeeklyHours() {
        return assignedWeeklyHours;
    }

    public void setAssignedWeeklyHours(Map<LocalDate, Integer> assignedWeeklyHours) {
        this.assignedWeeklyHours = assignedWeeklyHours;
    }

    public Long getProjectID() {
        return projectID;
    }

    public void setProjectID(Long projectID) {
        this.projectID = projectID;
    }


}
