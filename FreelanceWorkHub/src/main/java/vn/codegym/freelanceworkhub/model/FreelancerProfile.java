package vn.codegym.freelanceworkhub.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.*;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class FreelancerProfile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @OneToOne
    @JoinColumn(name = "user_id", unique = true)
    private User user;
    @Lob
    @Column(columnDefinition = "TEXT")
    private String bio;

    @ElementCollection
    @CollectionTable(name = "Freelancer_skills", joinColumns = @JoinColumn(name = "FreelancerProfile_id"))
    @Column(name = "skill")
    private Set<String> skills = new HashSet<>();

    private String location;

    private String Availability;


}
