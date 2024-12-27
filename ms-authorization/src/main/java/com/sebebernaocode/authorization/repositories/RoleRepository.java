package com.sebebernaocode.authorization.repositories;

import com.sebebernaocode.authorization.entities.role.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {
    boolean existsRoleByName(String name);

    Optional<Role> findByName(String roleOperator);
}