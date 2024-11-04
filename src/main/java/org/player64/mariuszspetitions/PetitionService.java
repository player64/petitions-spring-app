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

    public boolean signPetition(Long id, User user) {
        Optional<Petition> petitionOptional = petitionRepository.findById(id);

        if (petitionOptional.isEmpty()) {
            return false; // Petition not found
        }

        Petition petition = petitionOptional.get();

        // Check if user with the same email already signed the petition
        boolean userAlreadySigned = petition.getSignUsers().stream()
                .anyMatch(existingUser -> existingUser.getEmail().equalsIgnoreCase(user.getEmail()));

        if (userAlreadySigned) {
            return false; // Email already exists
        }

        // Add user if not already signed
        petition.getSignUsers().add(user);
        petitionRepository.saveOrUpdate(petition);
        return true;
    }
}
