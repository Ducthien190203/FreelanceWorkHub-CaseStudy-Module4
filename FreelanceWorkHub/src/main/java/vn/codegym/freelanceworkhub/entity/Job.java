package vn.codegym.freelanceworkhub.entity;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Table(name = "jobs")
public class Job implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "employer_id", referencedColumnName = "id", nullable = false)
    private User employer;

    @Column(nullable = false)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    private BigDecimal budget;

    private String category;

    private String status; // e.g., Active, Closed, Pending

    @Column(name = "created_at", nullable = false)
    @Temporal(TemporalType.DATE)
    private Date createdAt;

    @OneToMany(mappedBy = "job", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Application> applications;

    @Transient // This field is not persisted in the database
    private Integer applicantsCount; // New field

    @Column(name = "responsibilities_str", columnDefinition = "TEXT")
    private String responsibilitiesStr;

    @Column(name = "requirements_str", columnDefinition = "TEXT")
    private String requirementsStr;

    @Column(name = "skills_str", columnDefinition = "TEXT")
    private String skillsStr;

    private String duration;

    private String experience;

    public Job() {
    }

    public Job(User employer, String title, String description, BigDecimal budget, String category, String status, Date createdAt) {
        this.employer = employer;
        this.title = title;
        this.description = description;
        this.budget = budget;
        this.category = category;
        this.status = status;
        this.createdAt = createdAt;
    }

    // Getters and Setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public User getEmployer() {
        return employer;
    }

    public void setEmployer(User employer) {
        this.employer = employer;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getBudget() {
        return budget;
    }

    public void setBudget(BigDecimal budget) {
        this.budget = budget;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public List<Application> getApplications() {
        return applications;
    }

    public void setApplications(List<Application> applications) {
        this.applications = applications;
    }

    public Integer getApplicantsCount() {
        return applicantsCount;
    }

    public void setApplicantsCount(Integer applicantsCount) {
        this.applicantsCount = applicantsCount;
    }

    public List<String> getResponsibilities() {
        return responsibilitiesStr != null ? Arrays.asList(responsibilitiesStr.split(",")) : null;
    }

    public void setResponsibilities(List<String> responsibilities) {
        this.responsibilitiesStr = responsibilities != null ? String.join(",", responsibilities) : null;
    }

    public List<String> getRequirements() {
        return requirementsStr != null ? Arrays.asList(requirementsStr.split(",")) : null;
    }

    public void setRequirements(List<String> requirements) {
        this.requirementsStr = requirements != null ? String.join(",", requirements) : null;
    }

    public List<String> getSkills() {
        return skillsStr != null ? Arrays.asList(skillsStr.split(",")) : null;
    }

    public void setSkills(List<String> skills) {
        this.skillsStr = skills != null ? String.join(",", skills) : null;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getExperience() {
        return experience;
    }

    public void setExperience(String experience) {
        this.experience = experience;
    }

    @Override
    public String toString() {
        return "Job{" +
                "id=" + id +
                ", employer=" + employer +
                ", title='" + title + "'" +
                ", description='" + description + "'" +
                ", budget=" + budget +
                ", category='" + category + "'" +
                ", status='" + status + "'" +
                ", createdAt=" + createdAt +
                ", applicantsCount=" + applicantsCount +
                ", responsibilitiesStr='" + responsibilitiesStr + "'" +
                ", requirementsStr='" + requirementsStr + "'" +
                ", skillsStr='" + skillsStr + "'" +
                ", duration='" + duration + "'" +
                ", experience='" + experience + "'" +
                '}';
    }
}
