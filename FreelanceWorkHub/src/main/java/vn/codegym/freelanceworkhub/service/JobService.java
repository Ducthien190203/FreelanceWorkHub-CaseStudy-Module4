package vn.codegym.freelanceworkhub.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import vn.codegym.freelanceworkhub.model.ApplicationStatus;
import vn.codegym.freelanceworkhub.model.Job;
import vn.codegym.freelanceworkhub.model.JobStatus;
import vn.codegym.freelanceworkhub.model.User;
import vn.codegym.freelanceworkhub.repository.ApplicationRepository;
import vn.codegym.freelanceworkhub.repository.JobRepository;

import java.util.List;
import java.util.Optional;

@Service
public class JobService {
    @Autowired
    private JobRepository jobRepository;
    @Autowired
    private ApplicationRepository applicationRepository;

    public List<Job> findAllJobs() {
        return jobRepository.findAll();
    }

    public Optional<Job> findJobById(long id) {
        return jobRepository.findById(id);
    }

    public Job saveJob(Job job) {
        return jobRepository.save(job);
    }

    public void deleteJob(Job job) {
        jobRepository.delete(job);
    }

    public long countCompletedJobsByFreelancer(User freelancer) {
        return applicationRepository.countByFreelancerAndStatus(freelancer, ApplicationStatus.ACCEPTED);
    }
    public List<Job> findRecentJobs(){
        return jobRepository.findTop5ByOrderByCreatedDateDesc();
    }
    public long countActiveJobsByEmployer(User employer) {
        return jobRepository.countByEmployerAndStatus(employer, JobStatus.PENDING);
    }
    public List<Job> findJobsByEmployer(User employer) {
        return jobRepository.findByEmployer(employer);
    }
    public Page<Job> searchJobs(String keyword, Pageable pageable) {
        return jobRepository.findAllByTitleContainsOrDescriptionContainsOrLocationContains(keyword, keyword, keyword, pageable);
    }

}
