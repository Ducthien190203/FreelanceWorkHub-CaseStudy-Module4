package vn.codegym.freelanceworkhub.entity;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Entity
@Table(name = "applications")
public class Application implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "job_id", referencedColumnName = "id", nullable = false)
    private Job job;

    @ManyToOne
    @JoinColumn(name = "freelancer_id", referencedColumnName = "id", nullable = false)
    private User freelancer;

    @Column(name = "cover_letter", columnDefinition = "TEXT")
    private String coverLetter;

    @Column(name = "created_at", nullable = false)
    @Temporal(TemporalType.DATE)
    private Date createdAt;

    private String status; // e.g., Pending, Accepted, Rejected

    private BigDecimal proposedRate; // New field

    public Application() {
    }

    public Application(Job job, User freelancer, String coverLetter, Date createdAt, String status, BigDecimal proposedRate) {
        this.job = job;
        this.freelancer = freelancer;
        this.coverLetter = coverLetter;
        this.createdAt = createdAt;
        this.status = status;
        this.proposedRate = proposedRate;
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

    public String getCoverLetter() {
        return coverLetter;
    }

    public void setCoverLetter(String coverLetter) {
        this.coverLetter = coverLetter;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public BigDecimal getProposedRate() {
        return proposedRate;
    }

    public void setProposedRate(BigDecimal proposedRate) {
        this.proposedRate = proposedRate;
    }

    @Override
    public String toString() {
        return "Application{"
                + "id=" + id +
                ", job=" + job +
                ", freelancer=" + freelancer +
                ", coverLetter='" + coverLetter + "'" +
                ", createdAt=" + createdAt +
                ", status='" + status + "'" +
                ", proposedRate=" + proposedRate +
                '}';
    }
}
