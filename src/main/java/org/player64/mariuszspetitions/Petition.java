package org.player64.mariuszspetitions;


import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;

@Setter
@Getter
public class Petition {
    private Long id;
    private String title;
    private String createdAt;
    private User createdBy;
    private ArrayList<User> signUsers = new ArrayList<>();

    public Petition() {
    }

    public Petition(String title, User createdBy) {
        this.title = title;
        this.createdBy = createdBy;
        this.createdAt = java.time.LocalDate.now().toString();
    }
}
