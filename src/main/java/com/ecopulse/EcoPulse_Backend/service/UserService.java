package com.ecopulse.EcoPulse_Backend.service;

import com.ecopulse.EcoPulse_Backend.dtos.UserDTO;
import com.ecopulse.EcoPulse_Backend.model.User;
import com.ecopulse.EcoPulse_Backend.repository.UserRepo;
import org.springframework.stereotype.Service;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;


import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;


@Service
public class UserService {

    @Autowired
    UserRepo repo;

    public UserDTO getUserProfile(String username) throws UsernameNotFoundException{
        User user = repo.findByUsernameAndIsVerifiedTrue(username)
                .orElseThrow(() -> new UsernameNotFoundException("USER NOT FOUND !!"));
        return new UserDTO(user);
    }

    public User getUserProfile2(String username) throws UsernameNotFoundException{
        User user = repo.findByUsernameAndIsVerifiedTrue(username)
                .orElseThrow(() -> new UsernameNotFoundException("USER NOT FOUND !!"));
        return user;
    }

    public List<UserDTO> searchUsers(String prefix, String currentUsername) {
        List<User> users = repo.findTop10ByUsernameStartingWithAndIsVerifiedTrue(prefix).stream()
                .filter(user -> !user.getUsername().equals(currentUsername))
                .toList();
        if (users.isEmpty()) {
            return List.of(); // Return empty list if no users found
        }
        return users.stream().map(UserDTO::new).toList(); // Convert to UserDTO list
    }


    public User findOrCreateUser(GoogleIdToken.Payload payload) {
        String email = payload.getEmail();
        Optional<User> userOpt = repo.findByEmail(email);
        return userOpt.orElseGet(() -> {
            User newUser = new User();
            newUser.setEmail(email);
            newUser.setFullName((String) payload.get("name"));
            newUser.setUsername((String) payload.get("email").toString().split("@")[0]); // Use email prefix as username
            newUser.setVerified(true); // Assuming Google users are verified
            newUser.setPassword(""); // No password needed for Google login
            newUser.setCreatedAt(LocalDateTime.now());
            return repo.save(newUser);
        });
    }

    public void removeUnverifiedUsers() {
        LocalDateTime cutoff = LocalDateTime.now().minusDays(1);
        List<User> unverifiedUsers = repo.findByIsVerifiedFalseAndCreatedAtBefore(cutoff);
        if (!unverifiedUsers.isEmpty()) {
            repo.deleteAll(unverifiedUsers);
        }
    }
}

