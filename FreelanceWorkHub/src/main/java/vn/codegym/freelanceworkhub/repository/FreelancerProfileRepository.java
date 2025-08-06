package vn.codegym.freelanceworkhub.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.codegym.freelanceworkhub.model.FreelancerProfile;
import vn.codegym.freelanceworkhub.model.User;
@Repository
public interface FreelancerProfileRepository extends JpaRepository<FreelancerProfile, Long> {
    FreelancerProfile findByUser(User user);
}
