package vn.codegym.freelanceworkhub.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FreelancerProfileDto {
    private long id;
    private Long userId;
    private String bio;
    private Set<String> skills;
    private String location;
    private String availability;
}