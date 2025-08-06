package vn.codegym.freelanceworkhub.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.codegym.freelanceworkhub.model.Job;
import vn.codegym.freelanceworkhub.model.JobCategory;
import vn.codegym.freelanceworkhub.model.JobStatus;
import vn.codegym.freelanceworkhub.model.User;

import java.util.List;

@Repository
public interface JobRepository extends JpaRepository<Job, Long> {
     long countByEmployerAndStatus(User employer, JobStatus status);
     List<Job> findByEmployer(User employer);
     List<Job> findTop5ByOrderByCreatedDateDesc();

    Page<Job> findAllByTitleContainsOrDescriptionContainsOrLocationContains(String title, String description, String location,Pageable pageable);
}
