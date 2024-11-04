package org.player64.mariuszspetitions;

import org.springframework.stereotype.Repository;

import jakarta.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

@Repository
public class PetitionRepository {
    private final List<Petition> petitions = new ArrayList<>();
    private final AtomicLong counter = new AtomicLong();


    public PetitionRepository() {
        User user1 = new User("John Doe", "john.doe@example.com");
        User user2 = new User("Jane Doe", "jane.doe@example.com");

        List<String> initialPetitionTitles = List.of(
                "Increase Student Study Spaces on Campus",
                "Extend Library Opening Hours During Exam Periods",
                "Provide Free Mental Health Resources for Students",
                "Reduce Tuition Fees for Online Learning",
                "Implement Recycling Bins in All Campus Buildings",
                "Ban Single-Use Plastics in Campus Cafeterias",
                "Introduce More Affordable Meal Plans for Students",
                "Offer Hybrid Learning Options for All Courses",
                "Improve Wi-Fi Connectivity Across Campus",
                "Increase Funding for Student Clubs and Societies"
        );
        for (int i = 0; i < initialPetitionTitles.size(); i++) {
            saveOrUpdate(new Petition(initialPetitionTitles.get(i), (i % 2 == 0) ? user1 : user2));
        }
    }

    public List<Petition> getAll() {
        return petitions;
    }

    public Petition saveOrUpdate(Petition petition) {
        if (petition.getId() == null) {
            petition.setId(counter.incrementAndGet());
            petitions.add(petition);
        } else {
            deleteById(petition.getId());
            petitions.add(petition);
        }
        return petition;
    }

    public void deleteById(Long id) {
        petitions.removeIf(p -> p.getId().equals(id));
    }

    public Optional<Petition> findById(Long id) {
        return petitions.stream().filter(p -> p.getId().equals(id)).findFirst();
    }

    public Optional<Petition> searchByTitle(String name) {
        return petitions.stream().filter(p -> p.getTitle().toLowerCase().startsWith(name.toLowerCase())).findFirst();
    }

    public void signPetition(Long id, User signUser) {
        Optional<Petition> petition = findById(id);
        petition.ifPresent(p -> {
                p.getSignUsers().add(signUser);
        });
    }
}
