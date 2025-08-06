package vn.codegym.freelanceworkhub.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.codegym.freelanceworkhub.model.JobCategory;

@Repository
public interface JobCategoryRepository extends JpaRepository<JobCategory, Long> {
}
