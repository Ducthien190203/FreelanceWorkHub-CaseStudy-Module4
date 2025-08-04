package vn.codegym.freelanceworkhub.service;

import vn.codegym.freelanceworkhub.entity.SavedJob;
import vn.codegym.freelanceworkhub.entity.User;
import vn.codegym.freelanceworkhub.repository.SavedJobRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SavedJobService {

    private final SavedJobRepository savedJobRepository;

    @Autowired
    public SavedJobService(SavedJobRepository savedJobRepository) {
        this.savedJobRepository = savedJobRepository;
    }

    public List<SavedJob> findSavedJobsByFreelancer(User freelancer) {
        return savedJobRepository.findByFreelancerOrderBySavedAtDesc(freelancer);
    }

    public SavedJob saveSavedJob(SavedJob savedJob) {
        return savedJobRepository.save(savedJob);
    }

    public void deleteSavedJob(Integer id) {
        savedJobRepository.deleteById(id);
    }
}
