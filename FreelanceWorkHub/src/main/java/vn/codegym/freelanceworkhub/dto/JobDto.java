
package vn.codegym.freelanceworkhub.dto;

import lombok.*;
import vn.codegym.freelanceworkhub.model.JobStatus;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Data
public class JobDto {
    private long id;
    private Long employerId;
    private String title;
    private String description;
    private double budget;
    private Long categoryId;
    private String categoryName;
    private JobStatus status;
    private LocalDate createdDate;
    private String location;
}
