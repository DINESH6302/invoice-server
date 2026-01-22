package com.invoice.service;

import com.invoice.repositorie.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserServices {

    private final UserRepository userRepo;
    private final PasswordEncoder passwordEncoder;
    @Autowired
    public UserServices(UserRepository userRepo, PasswordEncoder passwordEncoder) {
        this.userRepo = userRepo;
        this.passwordEncoder = passwordEncoder;
    }

}
