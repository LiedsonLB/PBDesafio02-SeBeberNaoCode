package com.sebebernaocode.authorization.services;

import com.sebebernaocode.authorization.entities.user.User;
import com.sebebernaocode.authorization.entities.user.dto.UserUpdateDto;
import com.sebebernaocode.authorization.entities.user.dto.UserUpdatePasswordDto;
import com.sebebernaocode.authorization.exceptions.EntityNotFoundException;
import com.sebebernaocode.authorization.exceptions.InvalidPasswordException;
import com.sebebernaocode.authorization.exceptions.UniqueViolationException;
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
            throw new UniqueViolationException(String.format("Email '%s' already exists.", user.getEmail()));
        }
    }

    @Transactional(readOnly = true)
    public User get(Long id) {
        return userRepository.findById(id)
                .orElseThrow(
                        () -> new EntityNotFoundException(String.format("User with id'%s' not found.", id))
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
                    throw new UniqueViolationException(String.format("Email '%s' already exists.", dto.getEmail()));

                user.setEmail(dto.getEmail());
            }
        }

        if (dto.getFirstName() != null)
            user.setFirstName(dto.getFirstName());

        if (dto.getLastName() != null)
            user.setLastName(dto.getLastName());
    }

    @Transactional
    public void updatePassword(Long id, UserUpdatePasswordDto dto){
        String newPassword = dto.getNewPassword();
        String confirmNewPassword = dto.getConfirmNewPassword();

        if (!newPassword.equals(confirmNewPassword))
            throw new InvalidPasswordException("New password and confirmation are different.");

        String password = dto.getPassword();
        User user = get(id);

        if (!password.equals(user.getPassword()))
            throw new InvalidPasswordException("Invalid password.");

        user.setPassword(newPassword);
    }
}
