package com.sebebernaocode.authorization.services;

import com.sebebernaocode.authorization.entities.user.User;
import com.sebebernaocode.authorization.entities.user.dto.UserUpdateDto;
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

    @Transactional(readOnly = true)
    public User get(Long id) {
        return userRepository.findById(id)
                .orElseThrow(
                        () -> new RuntimeException(String.format("User with id'%s' not found.", id))
                );
    }

    @Transactional()
    public void update(Long id, UserUpdateDto dto) {
        User user = get(id);

        if (dto.getEmail() != null) {
            boolean emailEquals = user.getEmail().equals(dto.getEmail());

            if (!emailEquals) {
                boolean emailExists = userRepository.existsUserByEmail(dto.getEmail());

                if (emailExists)
                    throw new RuntimeException(String.format("Email '%s' already exists.", dto.getEmail()));

                user.setEmail(dto.getEmail());
            }
        }

        if (dto.getFirstName() != null)
            user.setFirstName(dto.getFirstName());

        if (dto.getLastName() != null)
            user.setLastName(dto.getLastName());
    }
}
