package com.enset.authentification;

import com.enset.authentification.request.RoleRequest;
import com.enset.authentification.request.UserRequest;
import com.enset.authentification.services.AccountService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.ArrayList;

@SpringBootApplication
public class AuthentificationApplication {

    public static void main(String[] args) {
        SpringApplication.run(AuthentificationApplication.class, args);
    }

    @Bean
    CommandLineRunner start(AccountService accountService){
        return args -> {
            accountService.addRole(new RoleRequest("USER"));
            accountService.addRole(new RoleRequest("ADMIN"));
            accountService.addRole(new RoleRequest("CUSTOMER_MANAGER"));
            accountService.addRole(new RoleRequest("PRODUCT_MANAGER"));
            accountService.addRole(new RoleRequest("BILLS_MANAGER"));

            accountService.addUser(new UserRequest("user1","1234","user1@email.com",new ArrayList<>()));
            accountService.addUser(new UserRequest("user2","1234","user2@email.com",new ArrayList<>()));
            accountService.addUser(new UserRequest("user3","1234","user3@email.com",new ArrayList<>()));
            accountService.addUser(new UserRequest("admin","1234","admin@email.com",new ArrayList<>()));
            accountService.addUser(new UserRequest("manager1","1234","manager1@email.com",new ArrayList<>()));
            accountService.addUser(new UserRequest("manager2","1234","manager2@email.com",new ArrayList<>()));

        };
    }
}
