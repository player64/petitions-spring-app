package org.player64.mariuszspetitions;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserTest {

    @Test
    void userConstructorSetsNameAndEmail() {
        User user = new User(null,  null);
        assertEquals(null, user.getName());
        assertEquals(null, user.getEmail());
    }

    @Test
    void setNameUpdatesName() {
        User user = new User();
        user.setName("Jane Doe");
        assertEquals("Jane Doe", user.getName());
    }

    @Test
    void setEmailUpdatesEmail() {
        User user = new User();
        user.setEmail("jane.doe@example.com");
        assertEquals("jane.doe@example.com", user.getEmail());
    }

    @Test
    void defaultConstructorInitializesWithNullValues() {
        User user = new User();
        assertNull(user.getName());
        assertNull(user.getEmail());
    }
}