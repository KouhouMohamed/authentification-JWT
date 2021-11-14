package com.enset.authentification.serviceImpl;

import com.enset.authentification.response.UserResponse;
import com.enset.authentification.services.AccountService;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    private AccountService accountService;

    public UserDetailsServiceImpl(AccountService accountService) {
        this.accountService = accountService;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserResponse userResponse = accountService.getUserByName(username);

        Collection<GrantedAuthority> authorities = new ArrayList();
        userResponse.getRoles().forEach(roleResponse -> {
            authorities.add(new SimpleGrantedAuthority(roleResponse.getRole()));
        });

        // we return the User and Spring will compare username and password with those entered in authentication form and permissions
        return new User(userResponse.getName(),userResponse.getPassword(),authorities);
    }
}
