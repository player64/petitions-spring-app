package org.player64.mariuszspetitions;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/petitions")
public class PetitionController {
    private final PetitionService petitionService;

    @Autowired
    public PetitionController(PetitionService petitionService) {
        this.petitionService = petitionService;
    }

    @GetMapping
    public List<Petition> getAllPetitions() {
        return petitionService.getAllPetitions();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Petition> getPetitionById(@PathVariable Long id) {
        Optional<Petition> petition = petitionService.getPetitionById(id);
        return petition.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @GetMapping("/search/{title}")
    public ResponseEntity<Petition> getPetitionByTitle(@NotEmpty @PathVariable String title) {
        Optional<Petition> petition = petitionService.getPetitionByTitle(title);
        return petition.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @PostMapping
    public ResponseEntity<Void> createPetition(@Valid @RequestBody Petition petition) {
        petitionService.createPetition(petition);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("/sign/{id}")
    public ResponseEntity<Void> signPetition(@PathVariable Long id, @Valid @RequestBody User user) {
        petitionService.signPetition(id, user);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<String> handleValidationExceptions(MethodArgumentNotValidException ex) {
        return ResponseEntity.badRequest().body("Validation failed: " + ex.getBindingResult().toString());
    }
}