package vn.codegym.freelanceworkhub.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import vn.codegym.freelanceworkhub.model.*;
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
    public void deleteJobById(long id) {jobRepository.deleteById(id);}

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
    public Page<Job> searchJobs(String keyword, Double budget, JobCategory jobCategory,String location, Pageable pageable) {
        return jobRepository.searchJobs(keyword,keyword,budget,jobCategory,location,pageable);
    }

}
