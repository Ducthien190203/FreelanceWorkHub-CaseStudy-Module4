package vn.codegym.freelanceworkhub.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Application {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    @JoinColumn(name = "job_id")
    private Job job;

    @ManyToOne
    @JoinColumn(name = "freelancer_id")
    private User freelancer;

    @Lob
    @Column(columnDefinition = "TEXT")
    private String coverLetter;

    private LocalDate createdDate;

    @Enumerated(EnumType.STRING)
    private ApplicationStatus status;
}
