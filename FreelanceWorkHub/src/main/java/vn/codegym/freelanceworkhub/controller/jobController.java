package vn.codegym.freelanceworkhub.controller;

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
import vn.codegym.freelanceworkhub.dto.JobDto;
import vn.codegym.freelanceworkhub.model.*;
import vn.codegym.freelanceworkhub.repository.UserRepository;
import vn.codegym.freelanceworkhub.service.ApplicationService;
import vn.codegym.freelanceworkhub.service.JobCategoryService;
import vn.codegym.freelanceworkhub.service.JobService;
import vn.codegym.freelanceworkhub.service.SavedJobService;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.Date;
import java.util.Optional;

import static javafx.scene.input.KeyCode.J;

@Controller
@RequestMapping("/jobs")
public class jobController {
    private final JobService jobService;
    private final UserRepository userRepository;
    private final ApplicationService applicationService;
    private final SavedJobService savedJobService;
    private final JobCategoryService jobCategoryService;

    @Autowired
    public jobController(JobService jobService, UserRepository userRepository, ApplicationService applicationService, SavedJobService savedJobService) {
        this.jobService = jobService;
        this.userRepository = userRepository;
        this.applicationService = applicationService;
        this.savedJobService = savedJobService;
        this.jobCategoryService = new JobCategoryService();
    }

    @GetMapping
    public String listJobs(Model model, @RequestParam(value = "keyword", required = false) String keyword,
                           @RequestParam(value = "budget", required = false) double budget,
                           @RequestParam(value = "jobCategory", required = false) String jobCategoryName,
                           @RequestParam(value = "location", required = false) String location,
                           @RequestParam(value = "page", defaultValue = "0") int page,
                           @RequestParam(value = "size", defaultValue = "10") int size,
                           @RequestParam(value = "sortBy", defaultValue = "createdDate") String sortBy,
                           @RequestParam(value = "sortDir", defaultValue = "desc") String sortDir
                           ) {
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name())? Sort.by(sortBy).ascending(): Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page, size, sort);
        JobCategory jobCategory = jobCategoryService.findByNameContains(jobCategoryName);
        Page<Job> jobPage = jobService.searchJobs(keyword,budget,jobCategory,location,pageable);
        model.addAttribute("jobPage", jobPage);
        model.addAttribute("jobs", jobPage.getContent());
        model.addAttribute("keyword", keyword);
        model.addAttribute("selectedCategory", jobCategory.getName());
        model.addAttribute("seletedLocation", location);
        model.addAttribute("categories", jobCategoryService.findAllJobCategory());
        model.addAttribute("location", location);
        model.addAttribute("currentPage", page);
        model.addAttribute("pageSize", size);
        model.addAttribute("sortBy", sortBy);
        model.addAttribute("sortDir", sortDir);
        return "jobs/jobs";

    }
    @GetMapping("{id}")
    public String viewJob(@PathVariable Long id, Model model) {

        Optional<Job> job = jobService.findJobById(id);
        if (job.isPresent()) {
            model.addAttribute("job", job.get());
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            if (auth != null && auth.isAuthenticated() && auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_FREELANCER")) ) {
                User currentUser = userRepository.findByUsername(auth.getName());

                boolean isSaved = savedJobService.findSavedJobByFreelancer(currentUser).stream()
                        .anyMatch(savedJob -> savedJob.getJob().getId().equals(id));
                model.addAttribute("isSaved", isSaved);
            }
            return "jobs/job-detail";
        }else{
            return "redirect:/jobs";
        }
    }
    @GetMapping("/new")
    public String showCreateJobForm(Model model) {
        model.addAttribute("job", new JobDto());
        model.addAttribute("selectedCategory", jobCategoryService.findAllJobCategory());
        return "jobs/post-job";
    }
    @PostMapping("/new")
    public String createJob(@ModelAttribute("job") @Valid JobDto jobDto, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            return "jobs/post-job";
        }
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userRepository.findByUsername(auth.getName());

        Job job = new Job();
        job.setEmployer(user);
        job.setTitle(jobDto.getTitle());
        job.setDescription(jobDto.getDescription());
        job.setBudget(jobDto.getBudget());
        job.setCategory(jobCategoryService.findJobCategoryById(jobDto.getCategoryId()));
        job.setStatus(JobStatus.PENDING);
        job.setCreatedDate(jobDto.getCreatedDate());
        job.setLocation(jobDto.getLocation());
        jobService.saveJob(job);
        return "redirect:/employer/dashboard";
    }

    @GetMapping("/{id}/edit")
    public String showEditJobForm(@PathVariable Long id, Model model) {
        Optional<Job> jobOptional = jobService.findJobById(id);
        if (jobOptional.isPresent()) {
            Job job = jobOptional.get();
            JobDto jobDto = new JobDto();
            jobDto.setId(job.getId());
            jobDto.setTitle(job.getTitle());
            jobDto.setDescription(job.getDescription());
            jobDto.setBudget(job.getBudget());
            jobDto.setCategoryId(job.getCategory().getId());
            jobDto.setCategoryName(job.getCategory().getName());

            jobDto.setStatus(job.getStatus());
            jobDto.setCreatedDate(job.getCreatedDate());
            jobDto.setLocation(job.getLocation());
            model.addAttribute("jobStatus", JobStatus.values());
            model.addAttribute("job", jobDto);
            model.addAttribute("categories", jobCategoryService.findAllJobCategory());
            return "jobs/post-job";
        }else {
            return "redirect:/employer/dashboard";
        }
    }
    @PostMapping("/{id}/edit")
    public String updateJob(@PathVariable("id") Long id,@ModelAttribute("job") @Valid JobDto jobDto, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            return "jobs/post-job";
        }

        Optional<Job> jobOptional = jobService.findJobById(id);
        if (jobOptional.isPresent()) {
            Job job = jobOptional.get();
            job.setTitle(jobDto.getTitle());
            job.setDescription(jobDto.getDescription());
            job.setBudget(jobDto.getBudget());
            job.setCategory(jobCategoryService.findJobCategoryById(jobDto.getCategoryId()));
            job.setStatus(job.getStatus());
            job.setCreatedDate(jobDto.getCreatedDate());
            job.setLocation(jobDto.getLocation());
            jobService.saveJob(job);
        }
        return "redirect:/employer/dashboard";

    }
    @PostMapping("/{id}/delete")
    public String deleteJob(@PathVariable("id") Long id, Model model) {
        jobService.deleteJobById(id);
        return "redirect:/employer/dashboard";
    }
    @PostMapping("/{id}/close")
    public String closeJob(@PathVariable("id") Long id, Model model) {
        Optional<Job> jobOptional = jobService.findJobById(id);
        if (jobOptional.isPresent()) {
            Job job = jobOptional.get();
            job.setStatus(JobStatus.CLOSED);
            jobService.saveJob(job);
        }
        return "redirect:/employer/dashboard";
    }
    @PostMapping("/{id}/apply")
    public String applyJob(@PathVariable("id") Long id,@RequestParam("coverLetter") String coverLetter, Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User currenUser = userRepository.findByUsername(auth.getName());
        Optional<Job> jobOptional = jobService.findJobById(id);
        if (jobOptional.isPresent() && jobOptional.get().getStatus().equals(JobStatus.APPROVED) && currenUser != null) {
            Job job = jobOptional.get();
            Application application = new Application();
            application.setJob(job);
            application.setFreelancer(currenUser);
            application.setCoverLetter(coverLetter);
            application.setStatus(ApplicationStatus.PENDING);
            application.setCreatedDate(LocalDate.now());
            applicationService.save(application);

        }
        return "redirect:/jobs/{id}?applied";

    }
}
