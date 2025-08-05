package vn.codegym.freelanceworkhub.service;

import vn.codegym.freelanceworkhub.entity.Application;
import vn.codegym.freelanceworkhub.entity.Job;
import vn.codegym.freelanceworkhub.entity.User;
import vn.codegym.freelanceworkhub.repository.ApplicationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ApplicationService {

    private final ApplicationRepository applicationRepository;

    @Autowired
    public ApplicationService(ApplicationRepository applicationRepository) {
        this.applicationRepository = applicationRepository;
    }

    public List<Application> findAllApplications() {
        return applicationRepository.findAll();
    }

    public Optional<Application> findApplicationById(Integer id) {
        return applicationRepository.findById(id);
    }

    public Application saveApplication(Application application) {
        return applicationRepository.save(application);
    }

    public void deleteApplication(Integer id) {
        applicationRepository.deleteById(id);
    }

    public long countApplicationsByFreelancer(User freelancer) {
        return applicationRepository.countByFreelancer(freelancer);
    }

    public long countActiveApplicationsByFreelancer(User freelancer) {
        return applicationRepository.countByFreelancerAndStatus(freelancer, "Pending"); // Assuming 'Pending' is active
    }

    public List<Application> findApplicationsByFreelancer(User freelancer) {
        return applicationRepository.findByFreelancerOrderByCreatedAtDesc(freelancer);
    }

    public long countTotalApplicantsForJobs(List<Job> jobs) {
        return applicationRepository.countByJobIn(jobs);
    }

    public long countHiredFreelancersForJobs(List<Job> jobs) {
        return applicationRepository.countByJobInAndStatus(jobs, "Accepted");
    }
}
