package com.enset.authentification.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Collection;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserRequest {
    private String name;
    private String email;
    private String password;
    private Collection<RoleRequest> roles;

    public UserRequest(String name, String email, String password){
        this.name=name;this.email=email;this.password=password;this.roles=new ArrayList<>();
    }
}
