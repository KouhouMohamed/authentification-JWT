package com.enset.authentification.securityConfig;

import com.enset.authentification.JwtUtil;
import com.enset.authentification.filters.JwtAuthenticationFilter;
import com.enset.authentification.filters.JwtAuthorizationFilter;
import com.enset.authentification.serviceImpl.UserDetailsServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    private UserDetailsServiceImpl userDetailsService;

    public SecurityConfig(UserDetailsServiceImpl userDetailsService) {
        this.userDetailsService = userDetailsService;
    }


    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // disable csrf if we use stateful we have to enable csrf token
        http.csrf().disable();
        //say that we will use session stateless with a token JWT
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        // disable frame's block
        http.headers().frameOptions().disable();
        //activate login form (stateful security)
        //http.formLogin();

        http.authorizeRequests().antMatchers("/h2-console/**", JwtUtil.REFRESH_TOKEN_PATH+"/**","login/**").permitAll();
        /*
         We use annotations
        http.authorizeRequests().antMatchers(HttpMethod.POST,"/users/new/**").hasAuthority("ADMIN");
         */
        // All request should have authentication
        http.authorizeRequests().anyRequest().authenticated();

        /*Configure StateLess Security*/
        http.addFilter(new JwtAuthenticationFilter(authenticationManagerBean()));
        http.addFilterBefore(new JwtAuthorizationFilter(), UsernamePasswordAuthenticationFilter.class);
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }
}
