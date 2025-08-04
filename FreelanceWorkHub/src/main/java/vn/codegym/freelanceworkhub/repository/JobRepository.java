package vn.codegym.freelanceworkhub.repository;

import vn.codegym.freelanceworkhub.entity.Job;
import vn.codegym.freelanceworkhub.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

@Repository
public interface JobRepository extends JpaRepository<Job, Integer>, PagingAndSortingRepository<Job, Integer> {
    long countByEmployerAndStatus(User employer, String status);
    List<Job> findTop5ByOrderByCreatedAtDesc(); // For recent jobs
    List<Job> findByEmployer(User employer);

    @Query(value = "SELECT j FROM Job j WHERE " +
           "(:keyword IS NULL OR LOWER(j.title) LIKE LOWER(CONCAT('%', :keyword, '%')) OR LOWER(j.description) LIKE LOWER(CONCAT('%', :keyword, '%'))) AND " +
           "(:category IS NULL OR j.category = :category) AND " +
           "(:location IS NULL OR j.employer.employerProfile.location = :location) AND " +
           "(:minBudget IS NULL OR j.budget >= :minBudget) AND " +
           "(:maxBudget IS NULL OR j.budget <= :maxBudget)",
           countQuery = "SELECT COUNT(j) FROM Job j WHERE " +
                        "(:keyword IS NULL OR LOWER(j.title) LIKE LOWER(CONCAT('%', :keyword, '%')) OR LOWER(j.description) LIKE LOWER(CONCAT('%', :keyword, '%'))) AND " +
                        "(:category IS NULL OR j.category = :category) AND " +
                        "(:location IS NULL OR j.employer.employerProfile.location = :location) AND " +
                        "(:minBudget IS NULL OR j.budget >= :minBudget) AND " +
                        "(:maxBudget IS NULL OR j.budget <= :maxBudget)")
    Page<Job> searchJobs(@Param("keyword") String keyword,
                         @Param("category") String category,
                         @Param("location") String location,
                         @Param("minBudget") java.math.BigDecimal minBudget,
                         @Param("maxBudget") java.math.BigDecimal maxBudget,
                         Pageable pageable);
}