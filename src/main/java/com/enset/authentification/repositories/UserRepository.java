package com.enset.authentification.repositories;

import com.enset.authentification.entities.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<AppUser,Long> {
    AppUser findByName(String username);
    List<AppUser> findAll();
}
