package com.coopcredit.creditapplication.infrastructure.adapter.output.persistence;

import com.coopcredit.creditapplication.application.mapper.UserMapper;
import com.coopcredit.creditapplication.domain.model.User;
import com.coopcredit.creditapplication.domain.ports.output.UserRepositoryPorts;
import com.coopcredit.creditapplication.infrastructure.adapter.output.persistence.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class UserPersistenceAdapter implements UserRepositoryPorts {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    public User save(User user) {
        return userMapper.toDomain(
                userRepository.save(userMapper.toEntity(user))
        );
    }

    @Override
    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username)
                .map(userMapper::toDomain);
    }
}
