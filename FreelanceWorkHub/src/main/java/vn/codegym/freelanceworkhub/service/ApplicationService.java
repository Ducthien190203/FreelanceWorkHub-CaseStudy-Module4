package vn.codegym.freelanceworkhub.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vn.codegym.freelanceworkhub.model.Application;
import vn.codegym.freelanceworkhub.model.ApplicationStatus;
import vn.codegym.freelanceworkhub.model.Job;
import vn.codegym.freelanceworkhub.model.User;
import vn.codegym.freelanceworkhub.repository.ApplicationRepository;

import java.util.List;
import java.util.Optional;

@Service
public class ApplicationService {
    @Autowired
    private ApplicationRepository applicationRepository;

    public List<Application> findAllApplications(){
        return applicationRepository.findAll();
    }
    public Optional<Application> findApplicationById(Long applicationId){
        return applicationRepository.findById(applicationId);
    }

    public Application save(Application application){
        return applicationRepository.save(application);
    }

    public void deleteApplicationById(Long applicationId){
        applicationRepository.deleteById(applicationId);
    }
    public long countApplicationsByFreelancer(User freelancer){
        return applicationRepository.countByFreelancer(freelancer);
    }
    public long countActiveApplicationsByFreelancer(User freelancer){
        return applicationRepository.countByFreelancerAndStatus(freelancer, ApplicationStatus.PENDING);
    }
    public List<Application> findApplicationsByFreelancer(User freelancer){
        return applicationRepository.findByFreelancerOrderByCreatedAtDesc(freelancer);

    }
    public long countTotalApplicationsForJobs(List<Job> jobs){
        return applicationRepository.countByJobIn(jobs);
    }
    public long countHiredFreelancersForJobs(List<Job> jobs){
        return applicationRepository.countByJobInAndStatus(jobs, ApplicationStatus.ACCEPTED);
    }
}
