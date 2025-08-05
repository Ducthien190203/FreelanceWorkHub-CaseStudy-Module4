package vn.codegym.freelanceworkhub.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import vn.codegym.freelanceworkhub.model.JobStatus;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class JobDto {
    private long id;
    private Long employerId;
    private String title;
    private String description;
    private double budget;
    private Long categoryId;
    private String categoryName;
    private JobStatus status;
    private Date createdDate;
}
