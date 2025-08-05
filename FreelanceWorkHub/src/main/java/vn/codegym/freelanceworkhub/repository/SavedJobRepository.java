package vn.codegym.freelanceworkhub.repository;

import vn.codegym.freelanceworkhub.entity.SavedJob;
import vn.codegym.freelanceworkhub.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SavedJobRepository extends JpaRepository<SavedJob, Integer> {
    List<SavedJob> findByFreelancerOrderBySavedAtDesc(User freelancer);
}
