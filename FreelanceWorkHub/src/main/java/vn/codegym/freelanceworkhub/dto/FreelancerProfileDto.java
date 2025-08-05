package vn.codegym.freelanceworkhub.dto;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

public class FreelancerProfileDto {

    private Integer id;

    @NotEmpty(message = "Bio is required")
    private String bio;

    @NotEmpty(message = "Skills are required")
    private String skills;

    @NotEmpty(message = "Location is required")
    private String location;

    private Integer experienceYears;

    @NotEmpty(message = "Availability is required")
    private String availability;

    public FreelancerProfileDto() {
    }

    // Getters and Setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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
}
