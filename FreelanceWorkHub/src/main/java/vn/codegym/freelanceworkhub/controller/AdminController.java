package vn.codegym.freelanceworkhub.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import vn.codegym.freelanceworkhub.model.EmployerProfile;
import vn.codegym.freelanceworkhub.model.FreelancerProfile;
import vn.codegym.freelanceworkhub.model.Job;
import vn.codegym.freelanceworkhub.model.User;
import vn.codegym.freelanceworkhub.repository.UserRepository;
import vn.codegym.freelanceworkhub.service.ApplicationService;
import vn.codegym.freelanceworkhub.service.JobService;
import vn.codegym.freelanceworkhub.service.UserService;

import java.util.Optional;

@Controller
@RequestMapping("/admin")
public class AdminController {
    private final UserService userService;
    private final JobService jobService;
    private final UserRepository userRepository;

    @Autowired
    public AdminController(UserService userService, JobService jobService, ApplicationService applicationService, UserRepository userRepository) {
        this.userService = userService;
        this.jobService = jobService;
        this.userRepository = userRepository;
    }

    @GetMapping("/dashboard")
    public String adminDashboard(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        User currentUser = userService.findByUsername(username);

        model.addAttribute("currentUser", currentUser);
        model.addAttribute("totalUsers", userService.findAllUsers().size());
        model.addAttribute("activeJobs",jobService.findAllJobs().stream().filter(job -> job.getStatus().equals("ACTIVE")).count());
        model.addAttribute("user", userService.findAllUsers());
        model.addAttribute("latestSignups", userService.findLatestSignUp());
        return "admin/dashboard-admin";

    }
    @GetMapping("/users")
    public String listUsers(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        User currentUser = userService.findByUsername(username);
        model.addAttribute("currentUser", currentUser);
        model.addAttribute("user", userService.findAllUsers());
        return "admin/admin-users";
    }
    @GetMapping("/users/{id}")
    public String showUser(@PathVariable long id, Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        User adminUser = userService.findByUsername(username);

        Optional<User> optionalUser = userService.findById(id);
        if (optionalUser.isPresent()) {
            User users = optionalUser.get();
            model.addAttribute("user", users);
            model.addAttribute("currentUser", adminUser);
            if (users.getRole().equals("FREELANCER")) {
                FreelancerProfile profile = users.getFreelancerProfile();
                if(profile != null){
                    model.addAttribute("profile", profile);
                }else {
                    model.addAttribute("profile", new FreelancerProfile());
                }
            } else if (users.getRole().equals("EMPLOYER")) {
                EmployerProfile profile = users.getEmployerProfile();
                if(profile != null){
                    model.addAttribute("profile", profile);
                }else{
                    model.addAttribute("profile", new EmployerProfile());
                }

            }
            return "admin/admin-user-detail";
        }else {
            return "redirect:/admin/users";
        }
    }
    @PostMapping("/users/{id}/toggle-status")
    public String toggleStatus(@PathVariable long id, Model model) {
        Optional<User> optionalUser = userService.findById(id);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            user.setEnabled(!user.isEnabled());
            userRepository.save(user);
        }
        return "redirect:/admin/users/{id}";
    }
    @GetMapping("/jobs")
    public String listJobs(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        User currentUser = userRepository.findByUsername(username);
        model.addAttribute("currentUser", currentUser);
        model.addAttribute("jobs", jobService.findAllJobs());
        return "admin/admin-jobs";
    }
    @GetMapping("/jobs/{id}")
    public String showJob(@PathVariable long id, Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        User currentUser = userRepository.findByUsername(username);
        Optional<Job> jobOptional = jobService.findJobById(id);
        if (jobOptional.isPresent()) {
            Job job = jobOptional.get();
            model.addAttribute("job", job);
            model.addAttribute("currentUser", currentUser);
            return "admin/admin-job-detail";
        } else {
            return "redirect:/admin/jobs";
        }
    }
    @PostMapping("/jobs/{id}/delete")
    public String deleteJob(@PathVariable long id, Model model) {
        jobService.deleteJobById(id);
        return "redirect:/admin/jobs";
    }
}
