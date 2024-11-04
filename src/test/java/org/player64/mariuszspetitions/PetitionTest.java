package org.player64.mariuszspetitions;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class PetitionTest {
    private static Validator validator;

    @BeforeAll
    static void setUpValidator() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

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

    @Test
    void petitionConstructorSetsTitleAndCreatedBy() {
        Petition petition = new Petition(null, null);
        Set<ConstraintViolation<Petition>> violations = validator.validate(petition);

        // Expect two violations: one for title and one for createdBy
        assertEquals(2, violations.size());
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("title")));
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("createdBy")));
    }

    @Test
    void validPetitionShouldPassValidation() {
        User creator = new User("John Doe", "john.doe@example.com");
        Petition petition = new Petition("Valid Petition Title", creator);

        Set<ConstraintViolation<Petition>> violations = validator.validate(petition);

        // No violations expected for valid title and creator
        assertTrue(violations.isEmpty());
    }

    @Test
    void petitionWithEmptyTitleShouldFailValidation() {
        User creator = new User("John Doe", "john.doe@example.com");
        Petition petition = new Petition("", creator);

        Set<ConstraintViolation<Petition>> violations = validator.validate(petition);

        // Expect one violation for the empty title
        assertEquals(1, violations.size());
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("title")));
    }

    @Test
    void petitionWithNullCreatorShouldFailValidation() {
        Petition petition = new Petition("Some Title", null);

        Set<ConstraintViolation<Petition>> violations = validator.validate(petition);

        // Expect one violation for null createdBy
        assertEquals(1, violations.size());
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("createdBy")));
    }

    @Test
    void petitionWithInvalidCreatorEmailShouldFailValidation() {
        User invalidUser = new User("John Doe", "invalid-email");
        Petition petition = new Petition("Valid Title", invalidUser);

        Set<ConstraintViolation<Petition>> violations = validator.validate(petition);

        // Expect one violation for the invalid email in createdBy
        assertEquals(1, violations.size());
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("createdBy.email")));
    }

    @Test
    void petitionWithInvalidUserInSignUsersShouldFailValidation() {
        User creator = new User("Jane Doe", "jane.doe@example.com");
        Petition petition = new Petition("Valid Title", creator);

        // Add a user with an invalid email to signUsers
        User invalidUser = new User("John Doe", "invalid-email");
        petition.getSignUsers().add(invalidUser);

        Set<ConstraintViolation<Petition>> violations = validator.validate(petition);

        // Expect one violation for the invalid email in signUsers
        assertEquals(1, violations.size());
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("signUsers[0].email")));
    }
}