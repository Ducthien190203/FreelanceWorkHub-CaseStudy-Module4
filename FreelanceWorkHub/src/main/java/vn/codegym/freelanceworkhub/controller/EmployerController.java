package vn.codegym.freelanceworkhub.controller;

import vn.codegym.freelanceworkhub.entity.User;
import vn.codegym.freelanceworkhub.entity.Job;
import vn.codegym.freelanceworkhub.entity.EmployerProfile;
import vn.codegym.freelanceworkhub.dto.EmployerProfileDto;
import vn.codegym.freelanceworkhub.service.UserService;
import vn.codegym.freelanceworkhub.service.JobService;
import vn.codegym.freelanceworkhub.service.ApplicationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;
import java.util.List;

@Controller
@RequestMapping("/employer")
public class EmployerController {

    private final UserService userService;
    private final JobService jobService;
    private final ApplicationService applicationService;

    @Autowired
    public EmployerController(UserService userService, JobService jobService, ApplicationService applicationService) {
        this.userService = userService;
        this.jobService = jobService;
        this.applicationService = applicationService;
    }

    @GetMapping("/dashboard")
    public String employerDashboard(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUserName = authentication.getName();
        User currentUser = userService.findByEmail(currentUserName);

        List<Job> employerJobs = jobService.findJobsByEmployer(currentUser);

        model.addAttribute("currentUser", currentUser);
        model.addAttribute("activeJobs", jobService.countActiveJobsByEmployer(currentUser));
        model.addAttribute("totalApplicants", applicationService.countTotalApplicantsForJobs(employerJobs));
        model.addAttribute("hiredFreelancers", applicationService.countHiredFreelancersForJobs(employerJobs));
        
        java.math.BigDecimal totalBudget = employerJobs.stream()
                                .map(Job::getBudget)
                                .reduce(java.math.BigDecimal.ZERO, java.math.BigDecimal::add);
        model.addAttribute("totalBudget", totalBudget);
        
        model.addAttribute("myJobs", employerJobs);

        return "dashboard-employer";
    }

    @GetMapping("/company-profile")
    public String showEmployerProfile(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUserName = authentication.getName();
        User currentUser = userService.findByEmail(currentUserName);

        EmployerProfile profile = userService.findEmployerProfileByUser(currentUser);
        if (profile == null) {
            profile = new EmployerProfile();
            profile.setUser(currentUser);
        }

        EmployerProfileDto profileDto = new EmployerProfileDto();
        profileDto.setId(profile.getId());
        profileDto.setCompanyName(profile.getCompanyName());
        profileDto.setCompanyDescription(profile.getCompanyDescription());
        profileDto.setWebsite(profile.getWebsite());
        profileDto.setLocation(profile.getLocation());

        model.addAttribute("currentUser", currentUser);
        model.addAttribute("profile", profileDto);
        return "employer-company-profile";
    }

    @PostMapping("/company-profile")
    public String updateEmployerProfile(@ModelAttribute("profile") @Valid EmployerProfileDto profileDto, BindingResult result, Model model) {
        if (result.hasErrors()) {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String currentUserName = authentication.getName();
            User currentUser = userService.findByEmail(currentUserName);
            model.addAttribute("currentUser", currentUser);
            return "employer-company-profile";
        }

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUserName = authentication.getName();
        User currentUser = userService.findByEmail(currentUserName);

        EmployerProfile profile = userService.findEmployerProfileByUser(currentUser);
        if (profile == null) {
            profile = new EmployerProfile();
            profile.setUser(currentUser);
        }

        profile.setCompanyName(profileDto.getCompanyName());
        profile.setCompanyDescription(profileDto.getCompanyDescription());
        profile.setWebsite(profileDto.getWebsite());
        profile.setLocation(profileDto.getLocation());

        userService.saveEmployerProfile(profile);
        return "redirect:/employer/company-profile?success";
    }
}
