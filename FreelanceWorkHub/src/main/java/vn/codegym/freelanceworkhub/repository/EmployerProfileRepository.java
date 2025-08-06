package vn.codegym.freelanceworkhub.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.codegym.freelanceworkhub.model.EmployerProfile;
import vn.codegym.freelanceworkhub.model.User;

import java.util.List;
@Repository
public interface EmployerProfileRepository extends JpaRepository<EmployerProfile, Long> {
    EmployerProfile findByUser(User employer);
}
