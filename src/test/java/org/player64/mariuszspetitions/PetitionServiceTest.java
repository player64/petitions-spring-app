package org.player64.mariuszspetitions;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PetitionServiceTest {

    @Mock
    private PetitionRepository petitionRepository;

    @InjectMocks
    private PetitionService petitionService;

    public PetitionServiceTest() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getAllPetitionsReturnsAllPetitions() {
        List<Petition> petitions = List.of(new Petition("Title1", new User("User1", "user1@example.com")));
        when(petitionRepository.getAll()).thenReturn(petitions);

        List<Petition> result = petitionService.getAllPetitions();
        assertEquals(petitions, result);
    }

    @Test
    void getPetitionByIdReturnsPetitionWhenIdExists() {
        Petition petition = new Petition("Title", new User("User", "user@example.com"));
        when(petitionRepository.findById(1L)).thenReturn(Optional.of(petition));

        Optional<Petition> result = petitionService.getPetitionById(1L);
        assertTrue(result.isPresent());
        assertEquals(petition, result.get());
    }

    @Test
    void getPetitionByIdReturnsEmptyWhenIdDoesNotExist() {
        when(petitionRepository.findById(1L)).thenReturn(Optional.empty());

        Optional<Petition> result = petitionService.getPetitionById(1L);
        assertFalse(result.isPresent());
    }

    @Test
    void getPetitionByTitleReturnsPetitionWhenTitleExists() {
        Petition petition = new Petition("Title", new User("User", "user@example.com"));
        when(petitionRepository.searchByTitle("Title")).thenReturn(Optional.of(petition));

        Optional<Petition> result = petitionService.getPetitionByTitle("Title");
        assertTrue(result.isPresent());
        assertEquals(petition, result.get());
    }

    @Test
    void getPetitionByTitleReturnsEmptyWhenTitleDoesNotExist() {
        when(petitionRepository.searchByTitle("Title")).thenReturn(Optional.empty());

        Optional<Petition> result = petitionService.getPetitionByTitle("Title");
        assertFalse(result.isPresent());
    }

    @Test
    void createPetitionSavesPetition() {
        Petition petition = new Petition("Title", new User("User", "user@example.com"));
        petitionService.createPetition(petition);
        verify(petitionRepository, times(1)).saveOrUpdate(petition);
    }

    @Test
    void signPetitionAddsUserToPetitionWhenIdExists() {
        Petition petition = new Petition("Title", new User("User", "user@example.com"));
        when(petitionRepository.findById(1L)).thenReturn(Optional.of(petition));
        User user = new User("New User", "new.user@example.com");

        petitionService.signPetition(1L, user);
        verify(petitionRepository, times(1)).signPetition(1L, user);
    }

    @Test
    void signPetitionDoesNothingWhenIdDoesNotExist() {
        when(petitionRepository.findById(-1L)).thenReturn(Optional.empty());
        User user = new User("New User", "new.user@example.com");

        petitionService.signPetition(-1L, user);
        verify(petitionRepository, never()).signPetition(anyLong(), any(User.class));
    }
}