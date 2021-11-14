package com.enset.authentification.serviceImpl;

import com.enset.authentification.entities.AppRole;
import com.enset.authentification.entities.AppUser;
import com.enset.authentification.mappers.RoleMapper;
import com.enset.authentification.mappers.UserMapper;
import com.enset.authentification.repositories.RoleRepository;
import com.enset.authentification.repositories.UserRepository;
import com.enset.authentification.request.RoleRequest;
import com.enset.authentification.request.UserRequest;
import com.enset.authentification.response.RoleResponse;
import com.enset.authentification.response.UserResponse;
import com.enset.authentification.services.AccountService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Service
@Transactional
public class AccountServiceImpl implements AccountService {
    private UserRepository userRepository;
    private RoleRepository roleRepository;
    private RoleMapper roleMapper;
    private UserMapper userMapper;
    private PasswordEncoder passwordEncoder;

    public AccountServiceImpl(UserRepository userRepository, RoleRepository roleRepository,
                              RoleMapper roleMapper, UserMapper userMapper, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.roleMapper = roleMapper;
        this.userMapper = userMapper;
        this.passwordEncoder = passwordEncoder;
    }

    // Add a new user
    @Override
    public UserResponse addUser(UserRequest user) {
        AppUser newUser = userMapper.userRequestToUser(user);
        String pw = user.getPassword();

        UserResponse response;
        newUser.setRoles(new ArrayList<>());

        //add roles to the new user
        if(user.getRoles()!=null){
            user.getRoles().forEach(roleRequest -> {
                AppRole rl = roleRepository.findByRole(roleRequest.getRole());
                // we test if the role exist in our database
                if(rl != null)
                    newUser.getRoles().add(rl);
            });
        }
        // save the new user in database
        newUser.setPassword(passwordEncoder.encode(pw));
        this.userRepository.save(newUser);
        response = userMapper.appUserToUserResponse(newUser);
        return response;
    }

    @Override
    public RoleResponse addRole(RoleRequest role) {
        AppRole newRole = roleMapper.roleRequestToAppRole(role);
        roleRepository.save(newRole);
        return roleMapper.appRoleToRoleResponse(newRole);
    }

    @Override
    public UserResponse addRoleToUser(String username, String roleName) {
        // Get user and role by their names from database
        AppUser user = userRepository.findByName(username);
        AppRole role = roleRepository.findByRole(roleName);
        UserResponse response;
        // Test if the getting objects aren't null
        if (user != null){
            if (role != null){
                // add role to user's roles
                user.getRoles().add(role);
                this.userRepository.save(user);
                response = userMapper.appUserToUserResponse(user);
                return response;
            }
        }
        return null;
    }

    @Override
    public UserResponse getUserByName(String username) {
        AppUser user = userRepository.findByName(username);
        UserResponse response = userMapper.appUserToUserResponse(user);
        return response;
    }

    @Override
    public Collection<UserResponse> getAllUsers() {
        List<AppUser> users = userRepository.findAll();
        List<UserResponse> userResponses = new ArrayList<>();
        users.forEach(appUser -> {
            userResponses.add(userMapper.appUserToUserResponse(appUser));
        });
        return userResponses;
    }

    @Override
    public Collection<RoleResponse> getAllRoles() {
        List<AppRole> roles = roleRepository.findAll();
        List<RoleResponse> roleResponses = new ArrayList<>();
        roles.forEach(appRole -> {
            roleResponses.add(roleMapper.appRoleToRoleResponse(appRole));
        });
        return roleResponses;
    }
}
