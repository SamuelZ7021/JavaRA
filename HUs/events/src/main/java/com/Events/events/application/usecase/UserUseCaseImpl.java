package com.Events.events.application.usecase;

import com.Events.events.domain.model.User;
import com.Events.events.domain.ports.in.UserUseCase;
import com.Events.events.domain.ports.out.UserRepositoryPort;

import java.util.List;

public class UserUseCaseImpl implements UserUseCase {

    private final UserRepositoryPort userRepositoryPort;

    public UserUseCaseImpl(UserRepositoryPort userRepositoryPort) {
        this.userRepositoryPort = userRepositoryPort;
    }

    @Override
    public User create(User user) {
        return userRepositoryPort.save(user);
    }

    @Override
    public User findById(Long id) {
        return userRepositoryPort.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));
    }

    @Override
    public List<User> findAll() {
        return userRepositoryPort.findAll();
    }

    @Override
    public User update(Long id, User user) {
        User currentUser = findById(id);
        currentUser.setName(user.getName());
        currentUser.setEmail(user.getEmail());
        return userRepositoryPort.save(currentUser);
    }

    @Override
    public void delete(Long id) {
        findById(id);
        userRepositoryPort.deleteById(id);
    }
}