package vn.codegym.freelanceworkhub.entity;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "users")
public class User implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(name = "full_name", nullable = false)
    private String fullName;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String role; // FREELANCER, EMPLOYER, ADMIN

    @Column(nullable = false)
    private boolean enabled;

    private String avatarUrl; // New field

    @Column(name = "created_at", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private FreelancerProfile freelancerProfile;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private EmployerProfile employerProfile;

    public User() {
        this.createdAt = new Date(); // Initialize here
    }

    public User(String username, String password, String fullName, String email, String role, boolean enabled) {
        this.username = username;
        this.password = password;
        this.fullName = fullName;
        this.email = email;
        this.role = role;
        this.enabled = enabled;
        this.avatarUrl = "https://via.placeholder.com/40"; // Default avatar URL
        this.createdAt = new Date(); // Initialize here
    }

    // Getters and Setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public FreelancerProfile getFreelancerProfile() {
        return freelancerProfile;
    }

    public void setFreelancerProfile(FreelancerProfile freelancerProfile) {
        this.freelancerProfile = freelancerProfile;
    }

    public EmployerProfile getEmployerProfile() {
        return employerProfile;
    }

    public void setEmployerProfile(EmployerProfile employerProfile) {
        this.employerProfile = employerProfile;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username + "'" +
                ", password='" + password + "'" +
                ", fullName='" + fullName + "'" +
                ", email='" + email + "'" +
                ", role='" + role + "'" +
                ", enabled=" + enabled +
                ", avatarUrl='" + avatarUrl + "'" +
                ", createdAt=" + createdAt +
                '}';
    }
}
