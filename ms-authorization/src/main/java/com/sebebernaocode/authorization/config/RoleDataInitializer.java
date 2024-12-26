package com.sebebernaocode.authorization.config;

import com.sebebernaocode.authorization.entities.role.Role;
import com.sebebernaocode.authorization.repositories.RoleRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class RoleDataInitializer {
    private final RoleRepository roleRepository;

    @PostConstruct
    private void initializeRole(){
        String[] roles = {"ROLE_ADMIN", "ROLE_OPERATOR"};

        for (String role : roles){
            if (!roleRepository.existsRoleByName(role))
                roleRepository.save(new Role(role));
        }
    }
}
