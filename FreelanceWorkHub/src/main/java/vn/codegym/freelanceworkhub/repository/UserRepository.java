package vn.codegym.freelanceworkhub.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import vn.codegym.freelanceworkhub.model.User;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsername(String username);
    User findByEmail(String email);

}
