package com.riwi.vettrack.appoitmentService.infrastructure.adapters.in.rest;

import com.riwi.vettrack.appoitmentService.domain.model.Pet;
import com.riwi.vettrack.appoitmentService.domain.ports.in.CreatePetCommand;
import com.riwi.vettrack.appoitmentService.domain.ports.in.CreatePetUseCase;
import com.riwi.vettrack.appoitmentService.infrastructure.adapters.in.rest.dto.PetRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/pets")
@RequiredArgsConstructor
public class PetController {

    private final CreatePetUseCase createPetUseCase;

    @PostMapping
    public ResponseEntity<Pet> create(@RequestBody @Valid PetRequest request) {
        // Mapeamos DTO -> Command (Desacoplamiento)
        var command = new CreatePetCommand(
                request.getName(),
                request.getOwnerName(),
                request.getOwnerDocument(),
                request.getSpecies(),
                request.getRace(),
                request.getAge()
        );

        Pet createdPet = createPetUseCase.createPet(command);

        return ResponseEntity.status(HttpStatus.CREATED).body(createdPet);
    }

    @GetMapping
    public ResponseEntity<List<Pet>> listAll() {
        List<Pet> pets = createPetUseCase.listPets();

        if (pets.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(pets);
    }

    // Bonus: Obtener por ID (útil para detalles)
    @GetMapping("/{id}")
    public ResponseEntity<Pet> getById(@PathVariable Long id) {
        // Requeriría agregar getPetById al caso de uso,
        // pero por ahora el listAll cubre el requerimiento principal.
        return ResponseEntity.notFound().build();
    }
}