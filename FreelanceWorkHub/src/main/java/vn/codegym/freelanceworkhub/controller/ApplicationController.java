package vn.codegym.freelanceworkhub.controller;

import vn.codegym.freelanceworkhub.entity.Application;
import vn.codegym.freelanceworkhub.entity.Job;
import vn.codegym.freelanceworkhub.entity.User;
import vn.codegym.freelanceworkhub.service.ApplicationService;
import vn.codegym.freelanceworkhub.service.JobService;
import vn.codegym.freelanceworkhub.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.stream.Collectors;
import java.util.Optional;

@Controller
@RequestMapping("/employer/applicants")
public class ApplicationController {

    private final ApplicationService applicationService;
    private final JobService jobService;
    private final UserService userService;

    @Autowired
    public ApplicationController(ApplicationService applicationService, JobService jobService, UserService userService) {
        this.applicationService = applicationService;
        this.jobService = jobService;
        this.userService = userService;
    }

        @GetMapping
    public String listApplicants(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUserName = authentication.getName();
        User currentUser = userService.findByEmail(currentUserName);

        // Get jobs posted by the current employer
        List<Job> employerJobs = jobService.findJobsByEmployer(currentUser);

        // Get applications for these jobs
        List<Application> applications = employerJobs.stream()
                                            .flatMap(job -> job.getApplications().stream())
                                            .collect(Collectors.toList());

        model.addAttribute("currentUser", currentUser);
        model.addAttribute("applications", applications);
        return "employer-applicants";
    }

    @GetMapping("/{id}")
    public String applicantDetails(@PathVariable("id") Integer id, Model model) {
        Optional<Application> application = applicationService.findApplicationById(id);
        if (application.isPresent()) {
            model.addAttribute("application", application.get());
            return "applicant-detail";
        } else {
            return "redirect:/employer/applicants";
        }
    }

    @PostMapping("/{id}/accept")
    public String acceptApplication(@PathVariable("id") Integer id) {
        Optional<Application> applicationOptional = applicationService.findApplicationById(id);
        if (applicationOptional.isPresent()) {
            Application application = applicationOptional.get();
            application.setStatus("Accepted");
            applicationService.saveApplication(application);
        }
        return "redirect:/employer/applicants/{id}";
    }

    @PostMapping("/{id}/reject")
    public String rejectApplication(@PathVariable("id") Integer id) {
        Optional<Application> applicationOptional = applicationService.findApplicationById(id);
        if (applicationOptional.isPresent()) {
            Application application = applicationOptional.get();
            application.setStatus("Rejected");
            applicationService.saveApplication(application);
        }
        return "redirect:/employer/applicants/{id}";
    }
}
