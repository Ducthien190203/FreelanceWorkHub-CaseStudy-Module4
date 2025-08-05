package vn.codegym.freelanceworkhub.service;

import vn.codegym.freelanceworkhub.entity.Job;
import vn.codegym.freelanceworkhub.entity.User;
import vn.codegym.freelanceworkhub.repository.JobRepository;
import vn.codegym.freelanceworkhub.repository.ApplicationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class JobService {

    private final JobRepository jobRepository;
    private final ApplicationRepository applicationRepository;

    @Autowired
    public JobService(JobRepository jobRepository, ApplicationRepository applicationRepository) {
        this.jobRepository = jobRepository;
        this.applicationRepository = applicationRepository;
    }

    public List<Job> findAllJobs() {
        return jobRepository.findAll();
    }

    public Optional<Job> findJobById(Integer id) {
        return jobRepository.findById(id);
    }

    public Job saveJob(Job job) {
        return jobRepository.save(job);
    }

    public void deleteJob(Integer id) {
        jobRepository.deleteById(id);
    }

    public long countCompletedJobsByFreelancer(User freelancer) {
        return applicationRepository.countByFreelancerAndStatus(freelancer, "Accepted");
    }

    public BigDecimal calculateTotalEarningsByFreelancer(User freelancer) {
        return applicationRepository.findByFreelancerOrderByCreatedAtDesc(freelancer).stream()
                .filter(app -> "Accepted".equals(app.getStatus()))
                .map(app -> app.getJob().getBudget())
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public List<Job> findRecentJobs() {
        return jobRepository.findTop5ByOrderByCreatedAtDesc();
    }

    public long countActiveJobsByEmployer(User employer) {
        return jobRepository.countByEmployerAndStatus(employer, "Active");
    }

    public List<Job> findJobsByEmployer(User employer) {
        return jobRepository.findByEmployer(employer);
    }

    public Page<Job> searchJobs(String keyword, String category, String location, BigDecimal minBudget, BigDecimal maxBudget, Pageable pageable) {
        return jobRepository.searchJobs(keyword, category, location, minBudget, maxBudget, pageable);
    }
}
