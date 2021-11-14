package com.enset.authentification.services;

import com.enset.authentification.request.RoleRequest;
import com.enset.authentification.request.UserRequest;
import com.enset.authentification.response.RoleResponse;
import com.enset.authentification.response.UserResponse;

import java.util.Collection;

public interface AccountService {
    public UserResponse addUser(UserRequest user);
    public RoleResponse addRole(RoleRequest role);
    public UserResponse addRoleToUser(String username, String roleName);
    public UserResponse getUserByName(String username);
    public Collection<UserResponse> getAllUsers();
    public Collection<RoleResponse> getAllRoles();

}
