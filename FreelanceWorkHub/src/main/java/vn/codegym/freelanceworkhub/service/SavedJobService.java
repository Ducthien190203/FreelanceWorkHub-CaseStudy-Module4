package vn.codegym.freelanceworkhub.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vn.codegym.freelanceworkhub.model.SavedJob;
import vn.codegym.freelanceworkhub.model.User;
import vn.codegym.freelanceworkhub.repository.SavedJobRepository;

import java.util.List;

@Service
public class SavedJobService {
    @Autowired
    private SavedJobRepository savedJobRepository;

    public List<SavedJob> findSavedJobByFreelancer(User freelancer) {
        return savedJobRepository.findByFreelancerOrderBySavedAtDesc(freelancer);
    }
    public SavedJob saveSavedJob(SavedJob savedJob) {
        return savedJobRepository.save(savedJob);
    }
    public void deleteSavedJob(Long id) {
        savedJobRepository.deleteById(id);
    }
}
