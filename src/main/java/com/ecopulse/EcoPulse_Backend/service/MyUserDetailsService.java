package com.ecopulse.EcoPulse_Backend.service;


import com.ecopulse.EcoPulse_Backend.model.User;
import com.ecopulse.EcoPulse_Backend.model.UserPrinciple;
import com.ecopulse.EcoPulse_Backend.repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;


@Service
public class MyUserDetailsService implements UserDetailsService {

    @Autowired
    UserRepo repo;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<User> user = repo.findByEmailAndIsVerifiedTrue(email);
        if(user.isEmpty()){
            throw new UsernameNotFoundException("Username not found . 404");
        }
        return new UserPrinciple(user.get());
    }

}

