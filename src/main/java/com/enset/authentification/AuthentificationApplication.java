package com.enset.authentification;

import com.enset.authentification.request.RoleRequest;
import com.enset.authentification.request.UserRequest;
import com.enset.authentification.services.AccountService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;

@SpringBootApplication
// other way to secure access to methods (user annotation on methods)
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
public class AuthentificationApplication {

    public static void main(String[] args) {
        SpringApplication.run(AuthentificationApplication.class, args);
    }

    @Bean
    PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    CommandLineRunner start(AccountService accountService){
        return args -> {
            accountService.addRole(new RoleRequest("USER"));
            accountService.addRole(new RoleRequest("ADMIN"));
            accountService.addRole(new RoleRequest("CUSTOMER_MANAGER"));
            accountService.addRole(new RoleRequest("PRODUCT_MANAGER"));
            accountService.addRole(new RoleRequest("BILLS_MANAGER"));

            accountService.addUser(new UserRequest("user1","user1@email.com","1234",new ArrayList<>()));
            accountService.addUser(new UserRequest("user2","user2@email.com","1234",new ArrayList<>()));
            accountService.addUser(new UserRequest("user3","user3@email.com","1234",new ArrayList<>()));
            accountService.addUser(new UserRequest("admin","admin@email.com","1234",new ArrayList<>()));
            accountService.addUser(new UserRequest("manager1","manager1@email.com","1234",new ArrayList<>()));
            accountService.addUser(new UserRequest("manager2","manager2@email.com","1234",new ArrayList<>()));

            accountService.addRoleToUser("user1","USER");
            accountService.addRoleToUser("user2","USER");
            accountService.addRoleToUser("user3","USER");
            accountService.addRoleToUser("admin","USER");
            accountService.addRoleToUser("admin","ADMIN");

            accountService.addRoleToUser("manager1","PRODUCT_MANAGER");
            accountService.addRoleToUser("manager2","BILLS_MANAGER");

        };
    }
}
