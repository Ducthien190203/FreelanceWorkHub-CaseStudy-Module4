package vn.codegym.freelanceworkhub.controller;

import vn.codegym.freelanceworkhub.entity.User;
import vn.codegym.freelanceworkhub.entity.Job;
import vn.codegym.freelanceworkhub.entity.FreelancerProfile;
import vn.codegym.freelanceworkhub.entity.EmployerProfile;
import vn.codegym.freelanceworkhub.service.UserService;
import vn.codegym.freelanceworkhub.service.JobService;
import vn.codegym.freelanceworkhub.service.ApplicationService;
import vn.codegym.freelanceworkhub.repository.UserRepository;
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
import java.util.Optional;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final UserService userService;
    private final JobService jobService;
    private final ApplicationService applicationService;
    private final UserRepository userRepository;

    @Autowired
    public AdminController(UserService userService, JobService jobService, ApplicationService applicationService, UserRepository userRepository) {
        this.userService = userService;
        this.jobService = jobService;
        this.applicationService = applicationService;
        this.userRepository = userRepository;
    }

    @GetMapping("/dashboard")
    public String adminDashboard(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUserName = authentication.getName();
        User currentUser = userService.findByEmail(currentUserName);

        model.addAttribute("currentUser", currentUser);
        model.addAttribute("totalUsers", userService.findAllUsers().size());
        model.addAttribute("activeJobs", jobService.findAllJobs().stream().filter(job -> job.getStatus().equals("Active")).count());
        model.addAttribute("newUsersToday", userService.countNewUsersToday());
        model.addAttribute("monthlyRevenue", 0); // Placeholder: Requires a financial transaction system to calculate
        model.addAttribute("users", userService.findAllUsers());
        model.addAttribute("latestSignups", userService.findLatestSignups());

        return "dashboard-admin";
    }

    @GetMapping("/users")
    public String listUsers(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUserName = authentication.getName();
        User currentUser = userService.findByEmail(currentUserName);

        model.addAttribute("currentUser", currentUser);
        model.addAttribute("users", userService.findAllUsers());
        return "admin-users";
    }

    @GetMapping("/users/{id}")
    public String userDetails(@PathVariable("id") Integer id, Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUserName = authentication.getName();
        User adminUser = userService.findByEmail(currentUserName);

        Optional<User> userOptional = userRepository.findById(id);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            model.addAttribute("currentUser", adminUser);
            model.addAttribute("user", user);

            if ("FREELANCER".equals(user.getRole())) {
                FreelancerProfile profile = userService.findFreelancerProfileByUser(user);
                if (profile != null) {
                    model.addAttribute("completedJobs", profile.getCompletedJobs());
                    model.addAttribute("totalEarnings", profile.getTotalEarnings());
                    model.addAttribute("avgRating", profile.getAvgRating());
                    model.addAttribute("responseTime", profile.getResponseTime());
                } else {
                    model.addAttribute("completedJobs", 0);
                    model.addAttribute("totalEarnings", 0);
                    model.addAttribute("avgRating", 0);
                    model.addAttribute("responseTime", "N/A");
                }
            } else if ("EMPLOYER".equals(user.getRole())) {
                // Employer specific stats if needed
                model.addAttribute("completedJobs", 0); // Placeholder
                model.addAttribute("totalEarnings", 0); // Placeholder
                model.addAttribute("avgRating", 0); // Placeholder
                model.addAttribute("responseTime", "N/A"); // Placeholder
            }

            return "admin-user-detail";
        } else {
            return "redirect:/admin/users";
        }
    }

    @PostMapping("/users/{id}/toggle-status")
    public String toggleUserStatus(@PathVariable("id") Integer id) {
        Optional<User> userOptional = userRepository.findById(id);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            user.setEnabled(!user.isEnabled());
            userRepository.save(user);
        }
        return "redirect:/admin/users/{id}";
    }

    @GetMapping("/jobs")
    public String listJobs(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUserName = authentication.getName();
        User currentUser = userService.findByEmail(currentUserName);

        model.addAttribute("currentUser", currentUser);
        model.addAttribute("jobs", jobService.findAllJobs());
        return "admin-jobs";
    }

    @GetMapping("/jobs/{id}")
    public String jobDetails(@PathVariable("id") Integer id, Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUserName = authentication.getName();
        User adminUser = userService.findByEmail(currentUserName);

        Optional<Job> jobOptional = jobService.findJobById(id);
        if (jobOptional.isPresent()) {
            Job job = jobOptional.get();
            model.addAttribute("currentUser", adminUser);
            model.addAttribute("job", job);
            return "admin-job-detail";
        } else {
            return "redirect:/admin/jobs";
        }
    }

    @PostMapping("/jobs/{id}/delete")
    public String deleteJob(@PathVariable("id") Integer id) {
        jobService.deleteJob(id);
        return "redirect:/admin/jobs";
    }

    @GetMapping("/analytics")
    public String showAnalytics(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUserName = authentication.getName();
        User currentUser = userService.findByEmail(currentUserName);

        model.addAttribute("currentUser", currentUser);
        // Add analytics data here
        return "admin-analytics";
    }
}