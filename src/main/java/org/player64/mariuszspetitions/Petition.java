package org.player64.mariuszspetitions;


import lombok.Getter;
import lombok.Setter;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;

@Setter
@Getter
public class Petition {
    private Long id;

    @NotEmpty(message = "Title must not be empty")
    private String title;
    private String createdAt;

    @Valid
    @NotNull(message = "User must not be null")
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
