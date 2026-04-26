package com.springbatch.controller;

import com.springbatch.request.LoginRequest;
import com.springbatch.service.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired private AuthenticationManager authenticationManager;
    @Autowired private JwtService jwtService;

    @PostMapping("/generate-token")
    public ResponseEntity<?> generateToken(@RequestBody LoginRequest request) {
        if (this.doAuthenticate(request.getUsername(), request.getPassword())) {
            String token = jwtService.generateToken(request.getUsername());
            Map<String, ?> response = Collections.singletonMap("jwt-token", token);
            return ResponseEntity.ok(response);
        }
        return ResponseEntity.badRequest().body("Authentication failed!!");
    }

    private boolean doAuthenticate(String username, String password) {
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username, password);
        Authentication authenticate = authenticationManager.authenticate(authenticationToken);
        return authenticate.isAuthenticated();
    }




}
