package vn.codegym.freelanceworkhub.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.codegym.freelanceworkhub.model.Application;
import vn.codegym.freelanceworkhub.model.ApplicationStatus;
import vn.codegym.freelanceworkhub.model.Job;
import vn.codegym.freelanceworkhub.model.User;

import java.util.List;

@Repository
public interface ApplicationRepository extends JpaRepository<Application, Long> {
    long countByFreelancer(User freelancer);
    long countByFreelancerAndStatus(User freelancer, ApplicationStatus status);
    List<Application> findByFreelancerOrderByCreatedAtDesc(User freelancer);
    long countByJobIn(List<Job> jobs);
    long countByJobInAndStatus(List<Job> jobs, ApplicationStatus status);

}
