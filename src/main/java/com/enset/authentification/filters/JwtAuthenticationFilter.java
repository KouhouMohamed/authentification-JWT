package com.enset.authentification.filters;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.enset.authentification.JwtUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter{
    private AuthenticationManager authenticationManager;

    public JwtAuthenticationFilter(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    // executes when a user attempt to authenticate
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        // here we suppose that username and pw are sent in the path as variables
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username,password);
        // this method check if the entered credentials are correct
        return authenticationManager.authenticate(authenticationToken);
    }

    // execute when the user authenticate successfully
    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        // authResult is the result of attemptAuthentication method
        // get the authenticated user
        User user =(User) authResult.getPrincipal();

        // generate JWT
        Algorithm algorithm = Algorithm.HMAC256(JwtUtil.SECRET);
        String jwtAccessToken = JWT.create()
                .withSubject(user.getUsername())
                // expiration date = 5min after generated date
                .withExpiresAt(new Date(System.currentTimeMillis()+ JwtUtil.EXPIRATION_ACCESS_TOKEN))
                //the origine of request
                .withIssuer(request.getRequestURL().toString())
                // add roles as a claim (we convert list of authorities to list of string)
                .withClaim("roles",user.getAuthorities().stream().map(ga->ga.getAuthority()).collect(Collectors.toList()))
                .sign(algorithm);

        String jwtRefreshToken = JWT.create()
                .withSubject(user.getUsername())
                .withExpiresAt(new Date(System.currentTimeMillis()+JwtUtil.EXPIRATION_REFRESH_TOKEN))
                .withIssuer(request.getRequestURL().toString())
                .sign(algorithm);

        // send the token in response body as in json format
        Map<String,String> accessToken=new HashMap<>();
        accessToken.put("Access_Token",jwtAccessToken);
        accessToken.put("Refresh_Token",jwtRefreshToken);
        response.setContentType("application/json");
        new JsonMapper().writeValue(response.getOutputStream(),accessToken);
    }

}
