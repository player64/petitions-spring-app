package org.player64.mariuszspetitions;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PetitionService {
    private final PetitionRepository petitionRepository;

    @Autowired
    public PetitionService(PetitionRepository petitionRepository) {
        this.petitionRepository = petitionRepository;
    }

    public List<Petition> getAllPetitions() {
        return petitionRepository.getAll();
    }

    public Optional<Petition> getPetitionById(Long petitionId) {
        return petitionRepository.findById(petitionId);
    }

    public Optional<Petition> getPetitionByTitle(String title) {
        return petitionRepository.searchByTitle(title);
    }

    public void createPetition(Petition petition) {
        petitionRepository.saveOrUpdate(petition);
    }

    public void signPetition(Long id, User user) {
        Optional<Petition> petition = petitionRepository.findById(id);
        if (petition.isPresent()) {
            petitionRepository.signPetition(id, user);
        }
    }
}
