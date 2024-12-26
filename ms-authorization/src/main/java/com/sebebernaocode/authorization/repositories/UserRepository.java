package com.sebebernaocode.authorization.repositories;

import com.sebebernaocode.authorization.entities.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
  boolean existsUserByEmail(String email);

    Optional<User> findByEmail(String email);
}