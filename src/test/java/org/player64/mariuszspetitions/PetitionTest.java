package org.player64.mariuszspetitions;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PetitionTest {

    @Test
    void constructorSetsTitleAndCreatedBy() {
        User user = new User("John Doe", "john.doe@example.com");
        Petition petition = new Petition("Save the Forest", user);
        assertEquals("Save the Forest", petition.getTitle());
        assertEquals(user, petition.getCreatedBy());
        assertNotNull(petition.getCreatedAt());
    }

    @Test
    void defaultConstructorInitializesWithEmptySignUsers() {
        Petition petition = new Petition();
        assertNotNull(petition.getSignUsers());
        assertTrue(petition.getSignUsers().isEmpty());
    }

    @Test
    void addSignUserAddsUserToSignUsers() {
        Petition petition = new Petition();
        User user = new User("Jane Doe", "jane.doe@example.com");
        petition.getSignUsers().add(user);
        assertTrue(petition.getSignUsers().contains(user));
    }

    @Test
    void setTitleUpdatesTitle() {
        Petition petition = new Petition();
        petition.setTitle("New sandbox on the playground");
        assertEquals("New sandbox on the playground", petition.getTitle());
    }

    @Test
    void setCreatedByUpdatesCreatedBy() {
        Petition petition = new Petition();
        User user = new User("Jane Doe", "jane.doe@example.com");
        petition.setCreatedBy(user);
        assertEquals(user, petition.getCreatedBy());
    }

    @Test
    void createdAtIsSetToCurrentDate() {
        User user = new User("John Doe", "john.doe@example.com");
        Petition petition = new Petition("Save the Forest", user);
        assertEquals(java.time.LocalDate.now().toString(), petition.getCreatedAt());
    }
}