package vn.codegym.freelanceworkhub.entity;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "employer_profiles")
public class EmployerProfile implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @OneToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false)
    private User user;

    @Column(name = "company_name", nullable = false)
    private String companyName;

    @Column(name = "company_description", columnDefinition = "TEXT")
    private String companyDescription;

    private String website;

    private String location;

    public EmployerProfile() {
    }

    public EmployerProfile(User user, String companyName, String companyDescription, String website, String location) {
        this.user = user;
        this.companyName = companyName;
        this.companyDescription = companyDescription;
        this.website = website;
        this.location = location;
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

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getCompanyDescription() {
        return companyDescription;
    }

    public void setCompanyDescription(String companyDescription) {
        this.companyDescription = companyDescription;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    @Override
    public String toString() {
        return "EmployerProfile{" +
                "id=" + id +
                ", user=" + user +
                ", companyName='" + companyName + '\'' +
                ", companyDescription='" + companyDescription + '\'' +
                ", website='" + website + '\'' +
                ", location='" + location + '\'' +
                '}';
    }
}