package vn.codegym.freelanceworkhub.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import vn.codegym.freelanceworkhub.dto.FreelancerProfileDto;
import vn.codegym.freelanceworkhub.entity.Application;
import vn.codegym.freelanceworkhub.entity.FreelancerProfile;
import vn.codegym.freelanceworkhub.entity.SavedJob;
import vn.codegym.freelanceworkhub.entity.User;
import vn.codegym.freelanceworkhub.service.ApplicationService;
import vn.codegym.freelanceworkhub.service.JobService;
import vn.codegym.freelanceworkhub.service.SavedJobService;
import vn.codegym.freelanceworkhub.service.UserService;

import javax.validation.Valid;
import java.util.List;

@Controller
@RequestMapping("/freelancer")
public class FreelancerController {

    private final UserService userService;
    private final JobService jobService;
    private final ApplicationService applicationService;
    private final SavedJobService savedJobService;

    @Autowired
    public FreelancerController(UserService userService, JobService jobService, ApplicationService applicationService, SavedJobService savedJobService) {
        this.userService = userService;
        this.jobService = jobService;
        this.applicationService = applicationService;
        this.savedJobService = savedJobService;
    }

    @GetMapping("/dashboard")
    public String freelancerDashboard(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUserName = authentication.getName();
        User currentUser = userService.findByEmail(currentUserName);

        model.addAttribute("currentUser", currentUser);
        model.addAttribute("totalApplications", applicationService.countApplicationsByFreelancer(currentUser));
        model.addAttribute("activeApplications", applicationService.countActiveApplicationsByFreelancer(currentUser));
        model.addAttribute("completedJobs", jobService.countCompletedJobsByFreelancer(currentUser));
        model.addAttribute("totalEarnings", jobService.calculateTotalEarningsByFreelancer(currentUser));
        model.addAttribute("recentJobs", jobService.findRecentJobs());
        model.addAttribute("myApplications", applicationService.findApplicationsByFreelancer(currentUser));

        return "dashboard-freelancer";
    }

    @GetMapping("/profile")
    public String showFreelancerProfile(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUserName = authentication.getName();
        User currentUser = userService.findByEmail(currentUserName);

        FreelancerProfile profile = userService.findFreelancerProfileByUser(currentUser);
        if (profile == null) {
            profile = new FreelancerProfile();
            profile.setUser(currentUser);
        }

        FreelancerProfileDto profileDto = new FreelancerProfileDto();
        profileDto.setId(profile.getId());
        profileDto.setBio(profile.getBio());
        profileDto.setSkills(profile.getSkills());
        profileDto.setLocation(profile.getLocation());
        profileDto.setExperienceYears(profile.getExperienceYears());
        profileDto.setAvailability(profile.getAvailability());

        model.addAttribute("currentUser", currentUser);
        model.addAttribute("profile", profileDto);
        return "freelancer-profile";
    }

    @PostMapping("/profile")
    public String updateFreelancerProfile(@ModelAttribute("profile") @Valid FreelancerProfileDto profileDto, BindingResult result, Model model) {
        if (result.hasErrors()) {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String currentUserName = authentication.getName();
            User currentUser = userService.findByEmail(currentUserName);
            model.addAttribute("currentUser", currentUser);
            return "freelancer-profile";
        }

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUserName = authentication.getName();
        User currentUser = userService.findByEmail(currentUserName);

        FreelancerProfile profile = userService.findFreelancerProfileByUser(currentUser);
        if (profile == null) {
            profile = new FreelancerProfile();
            profile.setUser(currentUser);
        }

        profile.setBio(profileDto.getBio());
        profile.setSkills(profileDto.getSkills());
        profile.setLocation(profileDto.getLocation());
        profile.setExperienceYears(profileDto.getExperienceYears());
        profile.setAvailability(profileDto.getAvailability());

        userService.saveFreelancerProfile(profile);
        return "redirect:/freelancer/profile?success";
    }

    @GetMapping("/saved-jobs")
    public String showSavedJobs(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUserName = authentication.getName();
        User currentUser = userService.findByEmail(currentUserName);

        List<SavedJob> savedJobs = savedJobService.findSavedJobsByFreelancer(currentUser);
        model.addAttribute("currentUser", currentUser);
        model.addAttribute("savedJobs", savedJobs);
        return "freelancer-saved-jobs";
    }

    @GetMapping("/applications")
    public String showApplications(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUserName = authentication.getName();
        User currentUser = userService.findByEmail(currentUserName);

        List<Application> myApplications = applicationService.findApplicationsByFreelancer(currentUser);
        model.addAttribute("currentUser", currentUser);
        model.addAttribute("myApplications", myApplications);
        return "freelancer-applications";
    }
}
