package vn.codegym.freelanceworkhub.repository;

import vn.codegym.freelanceworkhub.entity.FreelancerProfile;
import vn.codegym.freelanceworkhub.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FreelancerProfileRepository extends JpaRepository<FreelancerProfile, Integer> {
    FreelancerProfile findByUser(User user);
}
