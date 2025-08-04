package vn.codegym.freelanceworkhub.controller;

import vn.codegym.freelanceworkhub.entity.Job;
import vn.codegym.freelanceworkhub.entity.SavedJob;
import vn.codegym.freelanceworkhub.entity.User;
import vn.codegym.freelanceworkhub.service.JobService;
import vn.codegym.freelanceworkhub.service.SavedJobService;
import vn.codegym.freelanceworkhub.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Date;
import java.util.Optional;

@Controller
@RequestMapping("/freelancer/saved-jobs")
public class SavedJobController {

    private final SavedJobService savedJobService;
    private final JobService jobService;
    private final UserService userService;

    @Autowired
    public SavedJobController(SavedJobService savedJobService, JobService jobService, UserService userService) {
        this.savedJobService = savedJobService;
        this.jobService = jobService;
        this.userService = userService;
    }

    @PostMapping("/{jobId}/save")
    public String saveJob(@PathVariable("jobId") Integer jobId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUserName = authentication.getName();
        User currentUser = userService.findByEmail(currentUserName);

        Optional<Job> jobOptional = jobService.findJobById(jobId);
        if (jobOptional.isPresent() && currentUser != null) {
            Job job = jobOptional.get();
            SavedJob savedJob = new SavedJob();
            savedJob.setJob(job);
            savedJob.setFreelancer(currentUser);
            savedJob.setSavedAt(new Date());
            savedJobService.saveSavedJob(savedJob);
        }
        return "redirect:/jobs/{jobId}?saved";
    }

    @PostMapping("/{id}/delete")
    public String deleteSavedJob(@PathVariable("id") Integer id) {
        savedJobService.deleteSavedJob(id);
        return "redirect:/freelancer/saved-jobs";
    }
}
