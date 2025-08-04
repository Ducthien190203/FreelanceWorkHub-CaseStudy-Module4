package vn.codegym.freelanceworkhub.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(nullable = false, unique = true)
    private String username;
    @Column(nullable = false)
    private String password;

    private String fullName;

    private String email;
    @Enumerated(EnumType.STRING)
    private UserRole role;

    private boolean enabled;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    private FreelancerProfile freelancerProfile;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    private EmployerProfile employerProfile;

    @OneToMany(mappedBy = "freelancer", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Application> applications = new ArrayList<>();

    @OneToMany(mappedBy = "employer", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Job> jobs = new ArrayList<>();
    @OneToMany(mappedBy = "freelancer")
    private List<SavedJob> savedJobs;
}
