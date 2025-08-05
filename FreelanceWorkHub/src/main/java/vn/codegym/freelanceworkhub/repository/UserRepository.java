package vn.codegym.freelanceworkhub.repository;

import vn.codegym.freelanceworkhub.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    User findByEmail(String email);
    User findByUsername(String username);
    long countByCreatedAtAfter(Date date);
}
