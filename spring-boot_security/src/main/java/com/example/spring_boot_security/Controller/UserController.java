package com.example.spring_boot_security.Controller;

import com.example.spring_boot_security.Model.Users;
import com.example.spring_boot_security.Sevice.JwtServices;
import com.example.spring_boot_security.Sevice.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

    @Autowired
    private JwtServices jwtServices;

    @Autowired
    private UserService service;

    @Autowired
    private AuthenticationManager authenticationManager;

    @PostMapping("/register")
    public Users register(@RequestBody Users users) {
        return service.save(users);
    }

    @PutMapping("/register")
    public Users update(@RequestBody Users users) {
        return service.save(users);
    }

    @PostMapping("/login")
    public String login(@RequestBody Users users) {
        Authentication authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(users.getUsername(),users.getPassword()));
        if (authentication.isAuthenticated())
            return jwtServices.generatetoken(users.getUsername()) ;
        else
            return "fails";

    }

}
