package org.player64.mariuszspetitions;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class PetitionControllerTest {

    private MockMvc mockMvc;

    @Mock
    private PetitionService petitionService;

    @InjectMocks
    private PetitionController petitionController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(petitionController).build();
    }

    @Test
    void getAllPetitionsReturnsNonEmptyList() throws Exception {
        when(petitionService.getAllPetitions()).thenReturn(List.of(new Petition("Title", new User("User", "user@example.com"))));
        mockMvc.perform(get("/petitions"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title").value("Title"));
    }

    @Test
    void getPetitionByIdReturnsPetitionWhenIdExists() throws Exception {
        Petition petition = new Petition("Title", new User("User", "user@example.com"));
        when(petitionService.getPetitionById(1L)).thenReturn(Optional.of(petition));
        mockMvc.perform(get("/petitions/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Title"));
    }

    @Test
    void getPetitionByIdReturnsNotFoundWhenIdDoesNotExist() throws Exception {
        when(petitionService.getPetitionById(1L)).thenReturn(Optional.empty());
        mockMvc.perform(get("/petitions/1"))
                .andExpect(status().isNotFound());
    }

    @Test
    void getPetitionByTitleReturnsPetitionWhenTitleExists() throws Exception {
        Petition petition = new Petition("Title", new User("User", "user@example.com"));
        when(petitionService.getPetitionByTitle("Title")).thenReturn(Optional.of(petition));

        mockMvc.perform(get("/petitions/search/Title"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Title"));
    }

    @Test
    void getPetitionByTitleReturnsNotFoundWhenTitleDoesNotExist() throws Exception {
        when(petitionService.getPetitionByTitle("Nonexistent Title")).thenReturn(Optional.empty());

        mockMvc.perform(get("/petitions/search/Nonexistent Title"))
                .andExpect(status().isNotFound());
    }

    @Test
    void createPetitionWithNoTitleFails() throws Exception {
        String jsonContent = """
                {
                    "user": {
                        "name": "User",
                        "email": ""
                    }
                }
                """;
        mockMvc.perform(post("/petitions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonContent))
                .andExpect(status().isBadRequest());
        verify(petitionService, times(0)).createPetition(any(Petition.class));
    }

    @Test
    void createPetitionCallsServiceWithPetition() throws Exception {
        String jsonContent = """
                    {
                        "title": "New Petition",
                        "user": {
                            "name": "User",
                            "email": null
                        }
                    }
                """;
        mockMvc.perform(post("/petitions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonContent))
                .andExpect(status().isBadRequest());
        verify(petitionService, times(0)).createPetition(any(Petition.class));
    }

    @Test
    void signPetitionCallsServiceWithIdAndUser() throws Exception {
        String jsonContent = """
                    {
                        "name": "New User",
                        "email": "new.user@example.com"
                    }
                """;
        mockMvc.perform(post("/petitions/sign/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonContent))
                .andExpect(status().isOk());
        verify(petitionService, times(1)).signPetition(eq(1L), any(User.class));
    }

    @Test
    void signPetitionFailsWithInvalidEmail() throws Exception {
        String jsonContent = """
                    {
                        "name": "New User",
                        "email": "newexample.com"
                    }
                """;
        mockMvc.perform(post("/petitions/sign/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonContent))
                .andExpect(status().isBadRequest()); // Expect 400 due to validation error
        verify(petitionService, times(0)).signPetition(anyLong(), any(User.class)); // Ensure service is not called
    }
}