
package vn.codegym.freelanceworkhub.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vn.codegym.freelanceworkhub.model.JobCategory;
import vn.codegym.freelanceworkhub.repository.JobCategoryRepository;

import java.util.List;

@Service
public class JobCategoryService {
    @Autowired
    private JobCategoryRepository jobCategoryRepository;
    public List<JobCategory> findAllJobCategory() {
        return jobCategoryRepository.findAll();
    }
    public JobCategory findJobCategoryById(Long id) {
        return jobCategoryRepository.findById(id).orElse(null);
    }
    public JobCategory save(JobCategory jobCategory) {
        return jobCategoryRepository.save(jobCategory);
    }
    public void delete(Long id) {
        jobCategoryRepository.deleteById(id);
    }

    public JobCategory findByNameContains(String name) {

        return jobCategoryRepository.findByNameContains(name);
    }
}
