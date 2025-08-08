package vn.codegym.freelanceworkhub.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import vn.codegym.freelanceworkhub.model.Application;
import vn.codegym.freelanceworkhub.model.ApplicationStatus;
import vn.codegym.freelanceworkhub.model.Job;
import vn.codegym.freelanceworkhub.model.User;
import vn.codegym.freelanceworkhub.service.ApplicationService;
import vn.codegym.freelanceworkhub.service.JobService;
import vn.codegym.freelanceworkhub.service.UserService;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/employer/applicants")
public class ApplicationController {
    private final ApplicationService applicationService;
    private final JobService jobService;
    private final UserService userService;


    public ApplicationController(ApplicationService applicationService, JobService jobService, UserService userService) {
        this.applicationService = applicationService;
        this.jobService = jobService;
        this.userService = userService;
    }

    @GetMapping
    public String listApplications(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        User currentUser = userService.findByUsername(username);
        List<Job> employerJobs = jobService.findJobsByEmployer(currentUser);

        List<Application> applications = employerJobs.stream().flatMap(job -> job.getApplications().stream()).collect(Collectors.toList());
        model.addAttribute("currentUser", currentUser);
        model.addAttribute("applications", applications);
        return "employer/employer-applicants";
    }
    @GetMapping("/{id}")
    public String applicantDetail(@PathVariable Long id, Model model) {
        Optional<Application> application = applicationService.findApplicationById(id);
        if (application.isPresent()) {
            model.addAttribute("application", application.get());
            return "applicant-detail";
        } else {
            return "redirect:/employer/applicants";
        }
    }
    @PostMapping("/{id}/accept")
    public String applicantAccept(@PathVariable Long id, Model model) {
        Optional<Application> applicationOptional = applicationService.findApplicationById(id);
        if (applicationOptional.isPresent()) {
            Application application = applicationOptional.get();
            application.setStatus(ApplicationStatus.ACCEPTED);
            applicationService.save(application);
        }
        return "redirect:/employer/applicants/{id}";
    }
    @PostMapping("/{id}/reject")
    public String applicantReject(@PathVariable Long id, Model model) {
        Optional<Application> applicationOptional = applicationService.findApplicationById(id);
        if (applicationOptional.isPresent()) {
            Application application = applicationOptional.get();
            application.setStatus(ApplicationStatus.REJECTED);
            applicationService.save(application);
        }
        return "redirect:/employer/applicants/{id}";
    }
}
