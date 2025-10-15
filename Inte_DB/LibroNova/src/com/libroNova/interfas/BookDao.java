package com.libroNova.interfas;

import java.util.List;
import java.util.Optional;

public interface BookDao<T, Integer> {
    T create(T object);
    T update(T object);
    boolean delete(Integer id);
    Optional<T> searchById(Integer id);
    List<T> getAll();
}