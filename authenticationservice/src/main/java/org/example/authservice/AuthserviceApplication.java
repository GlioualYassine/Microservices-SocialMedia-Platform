package org.example.authservice;

import org.example.authservice.models.Role;
import org.example.authservice.repository.RoleRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableAsync;

import java.util.List;

@SpringBootApplication
@EnableJpaAuditing
@EnableAsync
public class AuthserviceApplication {

    public static void main(String[] args) {
        SpringApplication.run(AuthserviceApplication.class, args);
    }

    @Bean
    public CommandLineRunner runner(RoleRepository repository){
        return (args) -> {
            if(repository.findAll().isEmpty())
                repository.save(Role.builder().name("USER").build());
        };
    }
}
