package vn.codegym.freelanceworkhub.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EmployerProfileDto {
    private long id;
    private String companyName;
    private String companyDescription;
    private Long userId;
    private String website;
    private String location;
}
