package com.libroNova.interfas;

import com.libroNova.model.User;

import java.util.List;
import java.util.Optional;

public interface UserDao {
    User create(User user);
    User update(User user);
    boolean delete(Integer id);
    Optional<User> searchById(Integer id);
    List<User> getAll();
    Optional<User> findByEmail(String email);
}
