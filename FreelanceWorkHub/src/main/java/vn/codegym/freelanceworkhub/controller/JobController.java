package vn.codegym.freelanceworkhub.controller;

import vn.codegym.freelanceworkhub.dto.JobDto;
import vn.codegym.freelanceworkhub.entity.Job;
import vn.codegym.freelanceworkhub.entity.User;
import vn.codegym.freelanceworkhub.entity.SavedJob;
import vn.codegym.freelanceworkhub.service.JobService;
import vn.codegym.freelanceworkhub.service.ApplicationService;
import vn.codegym.freelanceworkhub.service.SavedJobService;
import vn.codegym.freelanceworkhub.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import vn.codegym.freelanceworkhub.entity.Application;

@Controller
@RequestMapping("/jobs")
public class JobController {

    private final JobService jobService;
    private final UserRepository userRepository;
    private final ApplicationService applicationService;
    private final SavedJobService savedJobService;

    @Autowired
    public JobController(JobService jobService, UserRepository userRepository, ApplicationService applicationService, SavedJobService savedJobService) {
        this.jobService = jobService;
        this.userRepository = userRepository;
        this.applicationService = applicationService;
        this.savedJobService = savedJobService;
    }

    @GetMapping
    public String listJobs(@RequestParam(value = "keyword", required = false) String keyword,
                           @RequestParam(value = "category", required = false) String category,
                           @RequestParam(value = "location", required = false) String location,
                           @RequestParam(value = "minBudget", required = false) BigDecimal minBudget,
                           @RequestParam(value = "maxBudget", required = false) BigDecimal maxBudget,
                           @RequestParam(value = "page", defaultValue = "0") int page,
                           @RequestParam(value = "size", defaultValue = "10") int size,
                           @RequestParam(value = "sortBy", defaultValue = "createdAt") String sortBy,
                           @RequestParam(value = "sortDir", defaultValue = "desc") String sortDir,
                           Model model) {

        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page, size, sort);

        Page<Job> jobPage = jobService.searchJobs(keyword, category, location, minBudget, maxBudget, pageable);
        model.addAttribute("jobPage", jobPage);
        model.addAttribute("jobs", jobPage.getContent()); // For compatibility with existing th:each
        model.addAttribute("keyword", keyword);
        model.addAttribute("selectedCategory", category);
        model.addAttribute("selectedLocation", location);
        model.addAttribute("minBudget", minBudget);
        model.addAttribute("maxBudget", maxBudget);
        model.addAttribute("categories", Arrays.asList("Web Development", "Design & Creative", "Writing & Content", "Digital Marketing", "Data & Analytics"));
        model.addAttribute("locations", Arrays.asList("Remote", "United States", "Europe", "Asia"));
        model.addAttribute("currentPage", page);
        model.addAttribute("pageSize", size);
        model.addAttribute("sortBy", sortBy);
        model.addAttribute("sortDir", sortDir);

        return "jobs";
    }

    @GetMapping("/{id}")
    public String jobDetails(@PathVariable("id") Integer id, Model model) {
        Optional<Job> job = jobService.findJobById(id);
        if (job.isPresent()) {
            model.addAttribute("job", job.get());

            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication != null && authentication.isAuthenticated() && authentication.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_FREELANCER"))) {
                User currentUser = userRepository.findByEmail(authentication.getName()); // Corrected
                boolean isSaved = savedJobService.findSavedJobsByFreelancer(currentUser).stream()
                                                .anyMatch(savedJob -> savedJob.getJob().getId().equals(id));
                model.addAttribute("isSaved", isSaved);
            }
            return "job-detail";
        } else {
            return "redirect:/jobs"; // Or a 404 page
        }
    }

    @GetMapping("/new")
    public String showCreateJobForm(Model model) {
        model.addAttribute("job", new JobDto());
        model.addAttribute("categories", Arrays.asList("Web Development", "Design & Creative", "Writing & Content", "Digital Marketing", "Data & Analytics"));
        return "post-job";
    }

    @PostMapping("/new")
    public String createJob(@ModelAttribute("job") @Valid JobDto jobDto, BindingResult result) {
        if (result.hasErrors()) {
            return "post-job";
        }

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUserName = authentication.getName();
        User currentUser = userRepository.findByEmail(currentUserName);

        Job job = new Job();
        job.setTitle(jobDto.getTitle());
        job.setDescription(jobDto.getDescription());
        job.setBudget(jobDto.getBudget());
        job.setCategory(jobDto.getCategory());
        job.setStatus("Active"); // Default status
        job.setCreatedAt(new Date());
        job.setEmployer(currentUser);
        job.setResponsibilities(jobDto.getResponsibilities());
        job.setRequirements(jobDto.getRequirements());
        job.setSkills(jobDto.getSkills());
        job.setDuration(jobDto.getDuration());
        job.setExperience(jobDto.getExperience());

        jobService.saveJob(job);
        return "redirect:/employer/dashboard"; // Redirect to employer's dashboard after posting
    }

    @GetMapping("/{id}/edit")
    public String showEditJobForm(@PathVariable("id") Integer id, Model model) {
        Optional<Job> jobOptional = jobService.findJobById(id);
        if (jobOptional.isPresent()) {
            Job job = jobOptional.get();
            JobDto jobDto = new JobDto();
            jobDto.setId(job.getId());
            jobDto.setTitle(job.getTitle());
            jobDto.setDescription(job.getDescription());
            jobDto.setBudget(job.getBudget());
            jobDto.setCategory(job.getCategory());
            jobDto.setStatus(job.getStatus());
            jobDto.setResponsibilities(job.getResponsibilities());
            jobDto.setRequirements(job.getRequirements());
            jobDto.setSkills(job.getSkills());
            jobDto.setDuration(job.getDuration());
            jobDto.setExperience(job.getExperience());

            model.addAttribute("job", jobDto);
            model.addAttribute("categories", Arrays.asList("Web Development", "Design & Creative", "Writing & Content", "Digital Marketing", "Data & Analytics"));
            return "post-job"; // Reusing the post-job form for editing
        } else {
            return "redirect:/employer/dashboard";
        }
    }

    @PostMapping("/{id}/edit")
    public String updateJob(@PathVariable("id") Integer id, @ModelAttribute("job") @Valid JobDto jobDto, BindingResult result) {
        if (result.hasErrors()) {
            return "post-job";
        }

        Optional<Job> jobOptional = jobService.findJobById(id);
        if (jobOptional.isPresent()) {
            Job job = jobOptional.get();
            job.setTitle(jobDto.getTitle());
            job.setDescription(jobDto.getDescription());
            job.setBudget(jobDto.getBudget());
            job.setCategory(jobDto.getCategory());
            job.setStatus(jobDto.getStatus());
            job.setResponsibilities(jobDto.getResponsibilities());
            job.setRequirements(jobDto.getRequirements());
            job.setSkills(jobDto.getSkills());
            job.setDuration(jobDto.getDuration());
            job.setExperience(jobDto.getExperience());

            jobService.saveJob(job);
        }
        return "redirect:/employer/dashboard";
    }

    @PostMapping("/{id}/delete")
    public String deleteJob(@PathVariable("id") Integer id) {
        jobService.deleteJob(id);
        return "redirect:/employer/dashboard";
    }

    @PostMapping("/{id}/close")
    public String closeJob(@PathVariable("id") Integer id) {
        Optional<Job> jobOptional = jobService.findJobById(id);
        if (jobOptional.isPresent()) {
            Job job = jobOptional.get();
            job.setStatus("Closed");
            jobService.saveJob(job);
        }
        return "redirect:/employer/dashboard";
    }

    @PostMapping("/{jobId}/apply")
    public String applyForJob(@PathVariable("jobId") Integer jobId, @RequestParam("coverLetter") String coverLetter, @RequestParam("proposedRate") BigDecimal proposedRate) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUserName = authentication.getName();
        User currentUser = userRepository.findByEmail(currentUserName);

        Optional<Job> jobOptional = jobService.findJobById(jobId);
        if (jobOptional.isPresent() && currentUser != null) {
            Job job = jobOptional.get();
            Application application = new Application();
            application.setJob(job);
            application.setFreelancer(currentUser);
            application.setCoverLetter(coverLetter);
            application.setCreatedAt(new Date());
            application.setStatus("Pending"); // Default status
            application.setProposedRate(proposedRate);
            applicationService.saveApplication(application);
        }
        return "redirect:/jobs/{jobId}?applied";
    }
}
