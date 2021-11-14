package com.enset.authentification.controllers;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.enset.authentification.JwtUtil;
import com.enset.authentification.request.RoleRequest;
import com.enset.authentification.request.RoleUserForm;
import com.enset.authentification.request.UserRequest;
import com.enset.authentification.response.RoleResponse;
import com.enset.authentification.response.UserResponse;
import com.enset.authentification.services.AccountService;
import com.fasterxml.jackson.databind.json.JsonMapper;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.security.Principal;
import java.util.*;
import java.util.stream.Collectors;

@RestController
public class AccountController {
    private AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @PostMapping(path = "/users/new", produces = {MediaType.APPLICATION_JSON_VALUE}, consumes = {MediaType.APPLICATION_JSON_VALUE})
    @PostAuthorize("hasAuthority('ADMIN')")
    public UserResponse addUser(@RequestBody UserRequest user) {
        System.out.println(user);
        return accountService.addUser(user);
    }

    @PostMapping(path = "roles/new")
    @PostAuthorize("hasAuthority('ADMIN')")
    public RoleResponse addRole(@RequestBody RoleRequest role) {
        return accountService.addRole(role);
    }

    @GetMapping(path = "/users/{username}", produces = {MediaType.APPLICATION_JSON_VALUE})
    @PostAuthorize("hasAnyAuthority('ADMIN','USER')")
    public UserResponse getUser(@PathVariable String username) {
        return accountService.getUserByName(username);
    }

    @GetMapping(path = "users/all", produces = {MediaType.APPLICATION_JSON_VALUE})
    @PostAuthorize("hasAnyAuthority('ADMIN','USER')")
    public Collection<UserResponse> getAllUsers() {
        return accountService.getAllUsers();
    }

    @GetMapping(path = "roles/all", produces = {MediaType.APPLICATION_JSON_VALUE})
    @PostAuthorize("hasAnyAuthority('ADMIN','USER')")
    public Collection<RoleResponse> getAllRoles() {
        return accountService.getAllRoles();
    }

    @PostMapping(path = "users/addRoleToUser", produces = {MediaType.APPLICATION_JSON_VALUE})
    @PostAuthorize("hasAuthority('ADMIN')")
    public UserResponse addRoleToUser(@RequestBody RoleUserForm roleUserForm) {
        String username = roleUserForm.getUsername();
        String rolename = roleUserForm.getRolename();
        return accountService.addRoleToUser(username, rolename);
    }
    @GetMapping(path = "user/profile")
    @PostAuthorize("hasAnyAuthority('USER','ADMIN')")
    public UserResponse profile(Principal principal){
        return accountService.getUserByName(principal.getName());
    }
    @GetMapping(path = "/refreshToken")
    public void refreshToken(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String refreshToken = request.getHeader(JwtUtil.AUTH_HEADER);
        if (refreshToken != null && refreshToken.startsWith(JwtUtil.TOKEN_START)) {
            try {
                String jwt = refreshToken.substring(JwtUtil.TOKEN_START.length());
                Algorithm algorithm = Algorithm.HMAC256(JwtUtil.SECRET);
                JWTVerifier jwtVerifier = JWT.require(algorithm).build();
                DecodedJWT decodedJWT = jwtVerifier.verify(jwt);
                String username = decodedJWT.getSubject();

                UserResponse userResponse = accountService.getUserByName(username);
                String jwtAccessToken = JWT.create()
                        .withSubject(userResponse.getName())
                        // expiration date = 5min after generated date
                        .withExpiresAt(new Date(System.currentTimeMillis() + JwtUtil.EXPIRATION_ACCESS_TOKEN))
                        //the origine of request
                        .withIssuer(request.getRequestURL().toString())
                        // add roles as a claim (we convert list of authorities to list of string)
                        .withClaim("roles", userResponse.getRoles().stream().map(roleResponse -> roleResponse.getRole()).collect(Collectors.toList()))
                        .sign(algorithm);

                Map<String, String> accessToken = new HashMap<>();
                accessToken.put("Access_Token", jwt);
                accessToken.put("Refresh_Token", refreshToken);
                response.setContentType("application/json");
                new JsonMapper().writeValue(response.getOutputStream(), accessToken);
            } catch (Exception e) {
                response.setHeader("error-message", e.getMessage());
                response.sendError(HttpServletResponse.SC_FORBIDDEN);
            }
        } else {
            throw new RuntimeException("Refresh Token required");
        }
    }
}
