//package vn.codegym.freelanceworkhub.model;
//
//import lombok.AllArgsConstructor;
//import lombok.Getter;
//import lombok.NoArgsConstructor;
//import lombok.Setter;
//
//import javax.persistence.*;
//import java.util.ArrayList;
//import java.util.Date;
//import java.util.List;
//
//@Getter
//@Setter
//@NoArgsConstructor
//@AllArgsConstructor
//@Entity
//public class Job {
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private long id;
//
//    @ManyToOne
//    @JoinColumn(name = "employer_id")
//    private User employer;
//
//    private String title;
//    @Lob
//    @Column(columnDefinition = "TEXT")
//    private String description;
//
//    private double budget;
//
//    @ManyToOne
//    @JoinColumn(name = "category_id")
//    private JobCategory category;
//
//    @Enumerated(EnumType.STRING)
//    private JobStatus status;
//
//    private Date createdDate;
//
//    @OneToMany(mappedBy = "job", cascade = CascadeType.ALL, orphanRemoval = true)
//    private List<Application> applications = new ArrayList<>();
//    @ManyToMany(mappedBy = "savedJobs")
//    private List<User> savedByUsers;
//}
