package com.sebebernaocode.authorization.services;

import com.sebebernaocode.authorization.entities.user.User;
import com.sebebernaocode.authorization.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class UserService {
    private final UserRepository userRepository;

    @Transactional
    public User create(User user) {
        try {
            return userRepository.save(user);
        } catch (DataIntegrityViolationException e) {
            throw new RuntimeException(String.format("Email '%s' already exists.", user.getEmail()));
        }
    }
}
