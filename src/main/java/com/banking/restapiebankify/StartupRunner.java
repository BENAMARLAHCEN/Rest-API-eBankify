package com.banking.restapiebankify;

import com.banking.restapiebankify.model.Role;
import com.banking.restapiebankify.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class StartupRunner implements CommandLineRunner {

    private final RoleRepository roleRepository;

    @Override
    public void run(String... args) throws Exception {
        List<String> roles = List.of("ADMIN", "USER", "EMPLOYEE");
        roles.forEach(roleName -> roleRepository
                .findByName(roleName)
                .orElseGet(() -> roleRepository.save(new Role(null, roleName)))
        );
    }
}

