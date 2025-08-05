package vn.codegym.freelanceworkhub.dto;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

public class EmployerProfileDto {

    private Integer id;

    @NotEmpty(message = "Company name is required")
    private String companyName;

    @NotEmpty(message = "Company description is required")
    private String companyDescription;

    private String website;

    @NotEmpty(message = "Location is required")
    private String location;

    public EmployerProfileDto() {
    }

    // Getters and Setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getCompanyDescription() {
        return companyDescription;
    }

    public void setCompanyDescription(String companyDescription) {
        this.companyDescription = companyDescription;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}
