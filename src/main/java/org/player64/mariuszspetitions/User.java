package org.player64.mariuszspetitions;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class User {
    private String name;
    private String email;

    public User() {
    }

    public User(String name, String email) {
        this.name = name;
        this.email = email;
    }
}

