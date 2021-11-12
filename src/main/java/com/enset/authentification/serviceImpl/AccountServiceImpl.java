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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collection;
@Service
@Transactional
public class AccountServiceImpl implements AccountService {
    private UserRepository userRepository;
    private RoleRepository roleRepository;
    private RoleMapper roleMapper;
    private UserMapper userMapper;

    public AccountServiceImpl(UserRepository userRepository, RoleRepository roleRepository,
                              RoleMapper roleMapper, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.roleMapper = roleMapper;
        this.userMapper = userMapper;
    }

    // Add a new user
    @Override
    public UserResponse addUser(UserRequest user) {
        AppUser newUser = userMapper.UserRequestToUser(user);
        newUser.setRoles(new ArrayList<AppRole>());

        //add roles to the new user
        user.getRoles().forEach(roleRequest -> {
            AppRole rl = roleRepository.findByRole(roleRequest.getRole());
            // we test if the role exist in our database
            if(rl != null)
                newUser.getRoles().add(rl);
        });
        // save the new user in database
        this.userRepository.save(newUser);
        return userMapper.UserToUserResponse(newUser);
    }

    @Override
    public RoleResponse addRole(RoleRequest role) {
        AppRole newRole = roleMapper.roleRequestToAppRole(role);
        roleRepository.save(newRole);

        return roleMapper.appRoleToRoleResponse(newRole);
    }

    @Override
    public UserResponse addRoleToUser(Long userId, Long roleId) {
        return null;
    }

    @Override
    public UserResponse getUserByName(String username) {
        return null;
    }

    @Override
    public Collection<UserResponse> getAllUsers() {
        return null;
    }

    @Override
    public Collection<RoleResponse> getAllRoles() {
        return null;
    }
}
