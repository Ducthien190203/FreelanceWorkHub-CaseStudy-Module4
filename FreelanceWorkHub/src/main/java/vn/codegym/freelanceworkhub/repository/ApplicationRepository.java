package vn.codegym.freelanceworkhub.repository;

import vn.codegym.freelanceworkhub.entity.Application;
import vn.codegym.freelanceworkhub.entity.Job;
import vn.codegym.freelanceworkhub.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ApplicationRepository extends JpaRepository<Application, Integer> {
    long countByFreelancer(User freelancer);
    long countByFreelancerAndStatus(User freelancer, String status);
    List<Application> findByFreelancerOrderByCreatedAtDesc(User freelancer);
    long countByJobIn(List<Job> jobs);
    long countByJobInAndStatus(List<Job> jobs, String status);
}
