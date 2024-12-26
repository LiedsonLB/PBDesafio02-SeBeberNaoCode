package com.sebebernaocode.authorization.repositories;

import com.sebebernaocode.authorization.entities.role.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Long> {
    boolean existsRoleByName(String name);
}