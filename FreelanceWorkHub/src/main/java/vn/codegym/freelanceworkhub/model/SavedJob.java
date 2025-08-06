package vn.codegym.freelanceworkhub.model;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
public class SavedJob {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    @JoinColumn(name = "freelancer_id")
    private User freelancer;

    @ManyToOne
    @JoinColumn(name = "job_id")
    private Job job;

    private LocalDate savedAt;
}
