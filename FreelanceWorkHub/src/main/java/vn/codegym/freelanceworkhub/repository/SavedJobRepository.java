package vn.codegym.freelanceworkhub.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.codegym.freelanceworkhub.model.Job;
import vn.codegym.freelanceworkhub.model.SavedJob;
import vn.codegym.freelanceworkhub.model.User;

import java.util.List;

@Repository
public interface SavedJobRepository extends JpaRepository<SavedJob, Long> {
    List<SavedJob> findByFreelancerOrderBySavedAtDesc(User freelancer);
}
