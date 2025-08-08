package vn.codegym.freelanceworkhub.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import vn.codegym.freelanceworkhub.dto.EmployerProfileDto;
import vn.codegym.freelanceworkhub.model.EmployerProfile;
import vn.codegym.freelanceworkhub.model.Job;
import vn.codegym.freelanceworkhub.model.User;
import vn.codegym.freelanceworkhub.service.ApplicationService;
import vn.codegym.freelanceworkhub.service.JobService;
import vn.codegym.freelanceworkhub.service.UserService;

import javax.validation.Valid;
import java.util.List;

@Controller
@RequestMapping("/employer")
public class EmployerController {
    private final UserService userService;
    private final JobService jobService;
    private final ApplicationService applicationService;


    public EmployerController(UserService userService, JobService jobService, ApplicationService applicationService) {
        this.userService = userService;
        this.jobService = jobService;
        this.applicationService = applicationService;
    }

    @GetMapping("/dashboard")
    public String employerDashboard(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        User currentUser = userService.findByUsername(username);

        List<Job> employerJobs = jobService.findJobsByEmployer(currentUser);
        model.addAttribute("currentUser", currentUser);
        model.addAttribute("activeJobs", jobService.countActiveJobsByEmployer(currentUser));
        model.addAttribute("totalApplicants", applicationService.countTotalApplicationsForJobs(employerJobs));
        model.addAttribute("hiredFreelancer",applicationService.countHiredFreelancersForJobs(employerJobs));
        model.addAttribute("myJobs", employerJobs);
        return "employer/dashboard-employer";
    }
    @GetMapping("/company-profile")
    public String showEmployerProfile(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        User currentUser = userService.findByUsername(username);

        EmployerProfile profile = userService.findEmployerProfileByUser(currentUser);
        if (profile == null) {
            profile = new EmployerProfile();
            profile.setUser(currentUser);
        }
        EmployerProfileDto profileDto = new EmployerProfileDto();
        profileDto.setId(profile.getId());
        profileDto.setCompanyName(profile.getCompanyName());
        profileDto.setCompanyDescription(profile.getCompanyDescription());
        profileDto.setUserId(profile.getUser().getId());
        profileDto.setWebsite(profile.getWebsite());
        profileDto.setLocation(profile.getLocation());
        model.addAttribute("profile", profileDto);
        model.addAttribute("currentUser", currentUser);

        return "employer/employer-company-profile";
    }
    @PostMapping("/company-profile")
    public String updateEmployerProfile(@ModelAttribute("profile") @Valid EmployerProfileDto profileDto, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String username = authentication.getName();
            User currentUser = userService.findByUsername(username);
            model.addAttribute("currentUser", currentUser);
            return "employer/employer-company-profile";
        }
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        User currentUser = userService.findByUsername(username);
        EmployerProfile profile = userService.findEmployerProfileByUser(currentUser);
        if (profile == null) {
            profile = new EmployerProfile();
            profile.setUser(currentUser);
        }
        profile.setCompanyName(profileDto.getCompanyName());
        profile.setCompanyDescription(profileDto.getCompanyDescription());
        profile.setUser(currentUser);
        profile.setWebsite(profileDto.getWebsite());
        profile.setLocation(profileDto.getLocation());

        userService.saveEmployerProfile(profile);
        return "redirect:/employer/company-profile?success";
    }
}
