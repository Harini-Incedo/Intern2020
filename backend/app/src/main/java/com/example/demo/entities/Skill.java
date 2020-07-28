package com.example.demo.entities;

import javax.persistence.*;

@Entity
@Table(name = "skill")
public class Skill {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    @Column(name = "skillName")
    private String skillName;
    @Column(name = "totalWeeklyHours")
    private int totalWeeklyHours;
    @Column(name = "projectID")
    private long projectID;

    public long getId() {
        return id;
    }

    public Skill(String skillName, int totalWeeklyHours, Long projectID) {
        this.skillName = skillName;
        this.totalWeeklyHours = totalWeeklyHours;
        this.projectID = projectID;
    }

    public Skill() {
        super();
    }

    public String getSkillName() {
        return skillName;
    }

    public void setSkillName(String skillName) {
        this.skillName = skillName;
    }

    public int getTotalWeeklyHours() {
        return totalWeeklyHours;
    }

    public void setTotalWeeklyHours(int totalWeeklyHours) {
        this.totalWeeklyHours = totalWeeklyHours;
    }

    public Long getProjectID() {
        return projectID;
    }

    public void setProjectID(Long projectID) {
        this.projectID = projectID;
    }


}
