package vn.codegym.freelanceworkhub.entity;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "saved_jobs")
public class SavedJob implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "job_id", referencedColumnName = "id", nullable = false)
    private Job job;

    @ManyToOne
    @JoinColumn(name = "freelancer_id", referencedColumnName = "id", nullable = false)
    private User freelancer;

    @Column(name = "saved_at", nullable = false)
    @Temporal(TemporalType.DATE)
    private Date savedAt;

    public SavedJob() {
    }

    public SavedJob(Job job, User freelancer, Date savedAt) {
        this.job = job;
        this.freelancer = freelancer;
        this.savedAt = savedAt;
    }

    // Getters and Setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Job getJob() {
        return job;
    }

    public void setJob(Job job) {
        this.job = job;
    }

    public User getFreelancer() {
        return freelancer;
    }

    public void setFreelancer(User freelancer) {
        this.freelancer = freelancer;
    }

    public Date getSavedAt() {
        return savedAt;
    }

    public void setSavedAt(Date savedAt) {
        this.savedAt = savedAt;
    }

    @Override
    public String toString() {
        return "SavedJob{" +
               "id=" + id +
               ", jobTitle='" + (job != null ? job.getTitle() : "null") +
               ", freelancer='" + (freelancer != null ? freelancer.getFullName() : "null") +
               '}';
    }
}
