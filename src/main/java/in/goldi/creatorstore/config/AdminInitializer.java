package in.goldi.creatorstore.config;

import in.goldi.creatorstore.entities.Role;
import in.goldi.creatorstore.entities.User;
import in.goldi.creatorstore.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@RequiredArgsConstructor
public class AdminInitializer {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Value("${ADMIN_EMAIL}")
    private String adminEmail;

    @Value("${ADMIN_PASSWORD}")
    private String adminPassword;

    @Bean
    public CommandLineRunner createAdmin() {

        return args -> {

            if (userRepository.existsByEmailIgnoreCase(
                    adminEmail
            )) {

                return;
            }

            User admin = User.builder()
                    .name("CreatorStore Admin")
                    .email(adminEmail.toLowerCase())
                    .password(
                            passwordEncoder.encode(
                                    adminPassword
                            )
                    )
                    .role(Role.ADMIN)
                    .build();

            userRepository.save(admin);

            System.out.println(
                    "================================="
            );

            System.out.println(
                    "Admin account created successfully"
            );

            System.out.println(
                    "Admin Email: " + adminEmail
            );

            System.out.println(
                    "================================="
            );
        };
    }
}