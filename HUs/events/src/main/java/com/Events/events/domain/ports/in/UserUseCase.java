package com.Events.events.domain.ports.in;

import com.Events.events.domain.model.User;

import java.util.List;

public interface UserUseCase {
    User create(User user);
    User findById(Long id);
    List<User> findAll();
    User update(Long id, User user);
    void delete(Long id);
}
