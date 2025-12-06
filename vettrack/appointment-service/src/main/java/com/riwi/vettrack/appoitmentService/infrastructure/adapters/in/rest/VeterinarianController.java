package com.riwi.vettrack.appoitmentService.infrastructure.adapters.in.rest;

import com.riwi.vettrack.appoitmentService.domain.model.Veterinarian;
import com.riwi.vettrack.appoitmentService.domain.ports.in.VeterinarianManagementUseCase;
import com.riwi.vettrack.appoitmentService.infrastructure.adapters.in.rest.dto.VeterinarianRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/veterinarians")
@RequiredArgsConstructor
public class VeterinarianController {

    private final VeterinarianManagementUseCase veterinarianUseCase;

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<Veterinarian> create(@RequestBody @Valid VeterinarianRequest request) {
        // Por defecto activo, pero flexible
        Veterinarian domain = new Veterinarian(null, request.name(), request.email(), request.phone(), true);
        return ResponseEntity.status(HttpStatus.CREATED).body(veterinarianUseCase.create(domain));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Veterinarian> getById(@PathVariable Long id) {
        return ResponseEntity.ok(veterinarianUseCase.getById(id));
    }

    // Endpoint para actualizar (y cambiar estado a inactivo si se quiere)
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<Veterinarian> update(@PathVariable Long id, @RequestBody @Valid VeterinarianRequest request) {
        // Asumimos que el request no trae 'active', así que lo mantenemos o recibimos un DTO con active.
        // Para ser prácticos, vamos a permitir cambiar el estado enviando un query param o un DTO extendido.
        // Aquí asumimos que el update mantiene el usuario activo por defecto,
        // PERO usaremos el DELETE para inactivar.
        Veterinarian domain = new Veterinarian(id, request.name(), request.email(), request.phone(), true);
        return ResponseEntity.ok(veterinarianUseCase.update(id, domain));
    }

    // Endpoint para INACTIVAR (Soft Delete)
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        veterinarianUseCase.delete(id);
        return ResponseEntity.noContent().build();
    }
}