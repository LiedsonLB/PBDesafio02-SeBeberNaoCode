package com.sebebernaocode.authorization.services;

import com.sebebernaocode.authorization.entities.role.Role;
import com.sebebernaocode.authorization.entities.user.User;
import com.sebebernaocode.authorization.entities.user.dto.UserUpdateDto;
import com.sebebernaocode.authorization.entities.user.dto.UserUpdatePasswordDto;
import com.sebebernaocode.authorization.exceptions.EntityNotFoundException;
import com.sebebernaocode.authorization.exceptions.InvalidPasswordException;
import com.sebebernaocode.authorization.exceptions.UniqueViolationException;
import com.sebebernaocode.authorization.repositories.RoleRepository;
import com.sebebernaocode.authorization.repositories.UserRepository;
import com.sebebernaocode.authorization.subscribers.NotificationUser;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class UserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final NotificationUser notificationUser;
    private final PasswordEncoder encoder;

    @Transactional
    public User create(User user) {
        try {
            user.setPassword(encoder.encode(user.getPassword()));
            user.getRoles().add(roleRepository.findByName("ROLE_OPERATOR")
                    .orElseThrow(
                            () -> new EntityNotFoundException("Role 'OPERATOR' not found.")
                    ));
            notificationUser.publishRegistrationEmail(user);
            return userRepository.save(user);
        } catch (DataIntegrityViolationException e) {
            throw new UniqueViolationException(String.format("Email '%s' already exists.", user.getEmail()));
        }
    }

    @Transactional
    public void createAdmin() {
        if (!userRepository.existsUserByRolesName("ROLE_ADMIN")) {
            Role admin = roleRepository.findByName("ROLE_ADMIN")
                    .orElseThrow(
                            () -> new EntityNotFoundException("Role 'ADMIN' not found.")
                    );

            User user = create(new User("User", "Admin", "admin@mail.com", "admin1"));
            user.getRoles().add(admin);
            userRepository.save(user);
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

        notificationUser.publishChangeEmail(user);
    }

    @Transactional
    public void updatePassword(Long id, UserUpdatePasswordDto dto) {
        String newPassword = dto.getNewPassword();
        String confirmNewPassword = dto.getConfirmNewPassword();

        if (!newPassword.equals(confirmNewPassword))
            throw new InvalidPasswordException("New password and confirmation are different.");

        String password = dto.getPassword();
        User user = get(id);

        if (!encoder.matches(password, user.getPassword()))
            throw new InvalidPasswordException("Invalid password.");

        user.setPassword(encoder.encode(newPassword));
        notificationUser.publishChangePasswordEmail(user);
    }

    public User findByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(
                        () -> new EntityNotFoundException(String.format("User with email'%s' not found.", email))
                );
    }
}
