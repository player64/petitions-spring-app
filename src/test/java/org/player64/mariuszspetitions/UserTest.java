package org.player64.mariuszspetitions;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class UserTest {

    private static Validator validator;

    @BeforeAll
    static void setUpValidator() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void userConstructorSetsNameAndEmail() {
        User user = new User(null, null);
        Set<ConstraintViolation<User>> violations = validator.validate(user);

        // Expect two violations: one for name and one for email
        assertEquals(2, violations.size());
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("name")));
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("email")));
    }

    @Test
    void setNameUpdatesName() {
        User user = new User();
        user.setName("Jane Doe");

        // We validate the object after setting the name
        Set<ConstraintViolation<User>> violations = validator.validate(user);

        // Since only the name is set, expect a violation on the email field
        assertEquals(1, violations.size());
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("email")));
    }

    @Test
    void setEmailUpdatesEmail() {
        User user = new User();
        user.setEmail("jane.doe@example.com");

        // Validate the object with a valid email but without a name
        Set<ConstraintViolation<User>> violations = validator.validate(user);

        // Expect one violation for the missing name field
        assertEquals(1, violations.size());
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("name")));
    }

    @Test
    void invalidEmailShouldFailValidation() {
        User user = new User();
        user.setEmail("invalid-email");

        Set<ConstraintViolation<User>> violations = validator.validate(user);

        // Expect a violation for the invalid email format
        assertEquals(2, violations.size());  // Expect violations for both missing name and invalid email
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("email")));
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("name")));
    }
}