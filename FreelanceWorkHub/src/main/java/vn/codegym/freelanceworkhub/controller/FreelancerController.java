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
import vn.codegym.freelanceworkhub.dto.FreelancerProfileDto;
import vn.codegym.freelanceworkhub.model.Application;
import vn.codegym.freelanceworkhub.model.FreelancerProfile;
import vn.codegym.freelanceworkhub.model.SavedJob;
import vn.codegym.freelanceworkhub.model.User;
import vn.codegym.freelanceworkhub.service.ApplicationService;
import vn.codegym.freelanceworkhub.service.JobService;
import vn.codegym.freelanceworkhub.service.SavedJobService;
import vn.codegym.freelanceworkhub.service.UserService;

import javax.validation.Valid;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/freelancer")
public class FreelancerController {
    private final UserService userService;
    private final JobService jobService;
    private final ApplicationService applicationService;
    private final SavedJobService savedJobService;


    public FreelancerController(UserService userService, JobService jobService, ApplicationService applicationService, SavedJobService savedJobService) {
        this.userService = userService;
        this.jobService = jobService;
        this.applicationService = applicationService;
        this.savedJobService = savedJobService;
    }
    @GetMapping("/dashboard")
    public String freelancerDashboard(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String currentUserName = auth.getName();
        User currentUser = userService.findByUsername(currentUserName);
        model.addAttribute("currentUser", currentUser);
        model.addAttribute("totalApplications", applicationService.countApplicationsByFreelancer(currentUser));
        model.addAttribute("activeApplications", applicationService.countActiveApplicationsByFreelancer(currentUser));
        model.addAttribute("completedJobs", jobService.countCompletedJobsByFreelancer(currentUser));
        model.addAttribute("recentJobs", jobService.findRecentJobs());
        model.addAttribute("myApplications", applicationService.findApplicationsByFreelancer(currentUser));
        return "dashboard-freelancer";
    }
    @GetMapping("/profile")
    public String showFreelancerProfile(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String currentUserName = auth.getName();
        User currentUser = userService.findByUsername(currentUserName);

        FreelancerProfile profile = userService.findFreelancerProfileByUser(currentUser);
        if (profile == null) {
            profile = new FreelancerProfile();
            profile.setUser(currentUser);
        }
        FreelancerProfileDto profileDto = new FreelancerProfileDto();
        profileDto.setId(profile.getId());
        profileDto.setUserId(profile.getUser().getId());
        profileDto.setBio(profile.getBio());
        profileDto.setSkills(profile.getSkills());
        profileDto.setLocation(profile.getLocation());
        profileDto.setAvailability(profile.getAvailability());
        model.addAttribute("profile", profileDto);
        model.addAttribute("currentUser", currentUser);
        return "freelancer-profile";
    }
    @PostMapping("/profile")
    public String updateFreelancerProfile(Model model, @ModelAttribute("profile") @Valid FreelancerProfileDto profileDto, BindingResult result) {
        if (result.hasErrors()) {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            String currentUserName = auth.getName();
            User currentUser = userService.findByUsername(currentUserName);
            model.addAttribute("currentUser", currentUser);
            return "freelancer-profile";
        }
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String currentUserName = auth.getName();
        User currentUser = userService.findByUsername(currentUserName);

        FreelancerProfile profile = userService.findFreelancerProfileByUser(currentUser);
        profile.setBio(profileDto.getBio());
        profile.setUser(currentUser);

        profile.setSkills(profileDto.getSkills());
        profile.setLocation(profileDto.getLocation());
        profile.setAvailability(profileDto.getAvailability());

        userService.saveFreelancerProfile(profile);
        return "redirect:/freelancer/profile?success";
    }
    @GetMapping("/saved-jobs")
    public String showSavedJobs(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String currentUserName = auth.getName();
        User currentUser = userService.findByUsername(currentUserName);

        List<SavedJob> savedJobs = savedJobService.findSavedJobByFreelancer(currentUser);
        model.addAttribute("savedJobs", savedJobs);
        model.addAttribute("currentUser", currentUser);
        return "freelance-saved-jobs";
    }
    @GetMapping("/applications")
    public String showApplications(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String currentUserName = auth.getName();
        User currentUser = userService.findByUsername(currentUserName);
        List<Application> applications = applicationService.findApplicationsByFreelancer(currentUser);
        model.addAttribute("myApplications", applications);
        model.addAttribute("currentUser", currentUser);
        return "freelancer-applications";
    }
}
