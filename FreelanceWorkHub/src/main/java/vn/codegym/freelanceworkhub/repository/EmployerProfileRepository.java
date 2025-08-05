package vn.codegym.freelanceworkhub.repository;

import vn.codegym.freelanceworkhub.entity.EmployerProfile;
import vn.codegym.freelanceworkhub.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmployerProfileRepository extends JpaRepository<EmployerProfile, Integer> {
    EmployerProfile findByUser(User user);
}
