package com.roombooking.config;

import com.roombooking.model.Admin;
import com.roombooking.repository.AdminRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final AdminRepository adminRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        // Create default admin if not exists (replaces setup_admin_db.php)
        if (adminRepository.findByEmail("admin@luxuryhomes.com").isEmpty()) {
            Admin admin = Admin.builder()
                    .name("Master Admin")
                    .email("admin@luxuryhomes.com")
                    .password(passwordEncoder.encode("admin123"))
                    .build();
            adminRepository.save(admin);
            System.out.println("✅ Default admin created: admin@luxuryhomes.com / admin123");
        }
    }
}
