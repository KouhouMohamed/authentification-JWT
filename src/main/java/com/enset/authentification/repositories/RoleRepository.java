package com.enset.authentification.repositories;

import com.enset.authentification.entities.AppRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoleRepository extends JpaRepository<AppRole,Long> {
    AppRole findByRole(String role);
    List<AppRole> findAll();
}
