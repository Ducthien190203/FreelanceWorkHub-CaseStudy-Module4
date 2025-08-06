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
public class EmployerProfile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
//    private String companyName;
    @Lob
    @Column(columnDefinition = "TEXT")
    private String companyDescription;
    @OneToOne
    @JoinColumn(name = "user_id", unique = true)
    private User user;

    private String website;
    private String location;

}
