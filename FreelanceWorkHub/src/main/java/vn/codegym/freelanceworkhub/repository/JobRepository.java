package vn.codegym.freelanceworkhub.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
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

    @Query("SELECT j FROM Job j " +
            "WHERE (:title IS NULL OR LOWER(j.title) LIKE LOWER(CONCAT('%', :title, '%'))) " +
            "AND (:description IS NULL OR LOWER(j.description) LIKE LOWER(CONCAT('%', :description, '%'))) " +
            "AND (:budget IS NULL OR j.budget <= :budget) " +
            "AND (:category IS NULL OR j.category = :category) " +
            "AND (:location IS NULL OR LOWER(j.location) LIKE LOWER(CONCAT('%', :location, '%')))")
    Page<Job> searchJobs(
            @Param("title") String title,
            @Param("description") String description,
            @Param("budget") Double budget,
            @Param("category") JobCategory category,
            @Param("location") String location,Pageable pageable
    );
}