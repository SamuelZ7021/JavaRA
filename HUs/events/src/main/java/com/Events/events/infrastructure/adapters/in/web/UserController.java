package com.Events.events.infrastructure.adapters.in.web;

import com.Events.events.domain.model.User;
import com.Events.events.domain.ports.in.UserUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    private final UserUseCase userUseCase;

    public UserController(UserUseCase userUseCase) {
        this.userUseCase = userUseCase;
    }

    @PostMapping
    public ResponseEntity<User> create(@RequestBody User user){
        return ResponseEntity.ok(userUseCase.create(user));
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> findById(@PathVariable Long id){
        return ResponseEntity.ok(userUseCase.findById(id));
    }

    @GetMapping
    public ResponseEntity<List<User>> findAll(){
        return ResponseEntity.ok((userUseCase.findAll()));
    }

    @PutMapping("/{id}")
    public ResponseEntity<User> update(@PathVariable Long id, @RequestBody User user){
        return ResponseEntity.ok(userUseCase.update(id, user));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id){
        userUseCase.delete(id);
        return ResponseEntity.noContent().build();
    }
}
