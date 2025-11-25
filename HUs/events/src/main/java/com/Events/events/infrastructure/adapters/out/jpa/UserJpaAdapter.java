package com.Events.events.infrastructure.adapters.out.jpa;

import com.Events.events.domain.model.User;
import com.Events.events.domain.ports.out.UserRepositoryPort;
import com.Events.events.infrastructure.adapters.out.jpa.repository.SpringDataUserRepository;
import com.Events.events.infrastructure.mappers.UserMapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class UserJpaAdapter implements UserRepositoryPort {
    private final SpringDataUserRepository repository;
    private final UserMapper userMapper;

    public UserJpaAdapter(SpringDataUserRepository repository, UserMapper userMapper) {
        this.repository = repository;
        this.userMapper = userMapper;
    }

    @Override
    public Optional<User> findById(Long id) {
        return repository.findById(id).map(userMapper::toDomain);
    }

    @Override
    public User save(User user) {
        var entity = userMapper.toEntity(user);
        return userMapper.toDomain(repository.save(entity));
    }

    @Override
    public List<User> findAll() {
        return userMapper.toDomainList(repository.findAll());
    }

    @Override
    public void deleteById(Long id) {
        repository.deleteById(id);
    }
}