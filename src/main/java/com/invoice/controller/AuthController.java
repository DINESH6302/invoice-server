package com.invoice.controller;

import com.invoice.dto.Tokens;
import com.invoice.dto.LoginRequestDto;
import com.invoice.dto.SignUpRequestDto;
import com.invoice.service.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
@CrossOrigin(origins = "http://localhost:3000")
public class AuthController {

    private final AuthService authService;

    @Autowired
    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/signup")
    public ResponseEntity<Void> createUser(@RequestBody SignUpRequestDto requestDto) {
        authService.signUp(requestDto);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("/login")
    public ResponseEntity<Tokens> loginUser(@RequestBody LoginRequestDto requestDto) {
        Tokens tokens = authService.login(requestDto);

        return ResponseEntity.ok().body(tokens);
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logoutUser(HttpServletRequest request) {
        authService.logout(request.getCookies());

        return ResponseEntity.ok().build();
    }

    @PostMapping("/refresh")
    public ResponseEntity<Tokens> refreshToken(HttpServletRequest request) {
        Tokens tokens = authService.refresh(request.getCookies());

        return ResponseEntity.ok().body(tokens);
    }
}
