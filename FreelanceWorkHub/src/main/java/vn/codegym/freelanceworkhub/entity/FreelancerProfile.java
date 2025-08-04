package vn.codegym.freelanceworkhub.entity;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "freelancer_profiles")
public class FreelancerProfile implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @OneToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false)
    private User user;

    @Column(columnDefinition = "TEXT")
    private String bio;

    @Column(columnDefinition = "TEXT")
    private String skills;

    private String location;

    @Column(name = "experience_years")
    private Integer experienceYears;

    private String availability;

    private String title; // New field

    private Integer completedJobs;
    private Double avgRating;
    private String responseTime;
    private java.math.BigDecimal totalEarnings;

    public FreelancerProfile() {
    }

    public FreelancerProfile(User user, String bio, String skills, String location, Integer experienceYears, String availability) {
        this.user = user;
        this.bio = bio;
        this.skills = skills;
        this.location = location;
        this.experienceYears = experienceYears;
        this.availability = availability;
        this.title = ""; // Default title
        this.completedJobs = 0;
        this.avgRating = 0.0;
        this.responseTime = "N/A";
        this.totalEarnings = java.math.BigDecimal.ZERO;
    }

    // Getters and Setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public String getSkills() {
        return skills;
    }

    public void setSkills(String skills) {
        this.skills = skills;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Integer getExperienceYears() {
        return experienceYears;
    }

    public void setExperienceYears(Integer experienceYears) {
        this.experienceYears = experienceYears;
    }

    public String getAvailability() {
        return availability;
    }

    public void setAvailability(String availability) {
        this.availability = availability;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getCompletedJobs() {
        return completedJobs;
    }

    public void setCompletedJobs(Integer completedJobs) {
        this.completedJobs = completedJobs;
    }

    public Double getAvgRating() {
        return avgRating;
    }

    public void setAvgRating(Double avgRating) {
        this.avgRating = avgRating;
    }

    public String getResponseTime() {
        return responseTime;
    }

    public void setResponseTime(String responseTime) {
        this.responseTime = responseTime;
    }

    public java.math.BigDecimal getTotalEarnings() {
        return totalEarnings;
    }

    public void setTotalEarnings(java.math.BigDecimal totalEarnings) {
        this.totalEarnings = totalEarnings;
    }

    @Override
    public String toString() {
        return "FreelancerProfile{" +
                "id=" + id +
                ", user=" + user +
                ", bio='" + bio + "'"
                + ", skills='" + skills + "'"
                + ", location='" + location + "'"
                + ", experienceYears=" + experienceYears +
                ", availability='" + availability + "'"
                + ", title='" + title + "'"
                + ", completedJobs=" + completedJobs +
                ", avgRating=" + avgRating +
                ", responseTime='" + responseTime + "'"
                + ", totalEarnings=" + totalEarnings +
                '}';
    }
}