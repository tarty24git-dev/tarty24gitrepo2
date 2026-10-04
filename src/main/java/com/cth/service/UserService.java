package com.cth.service;

import com.cth.model.User;
import com.cth.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public User registerUser(String username, String password, String fullName, String email, String role) {
        if (userRepository.existsByUsername(username)) {
            throw new IllegalArgumentException("Username already exists");
        }
        User user = new User(username, passwordEncoder.encode(password), fullName, email, role != null ? role : "ROLE_USER");
        return userRepository.save(user);
    }

    public void lockUser(Long userId) {
        userRepository.findById(userId).ifPresent(user -> {
            user.setLocked(true);
            userRepository.save(user);
        });
    }

    public void unlockUser(Long userId) {
        userRepository.findById(userId).ifPresent(user -> {
            user.setLocked(false);
            userRepository.save(user);
        });
    }

    public void resetPassword(Long userId, String newPassword) {
        userRepository.findById(userId).ifPresent(user -> {
            user.setPassword(passwordEncoder.encode(newPassword));
            user.setPasswordLastChanged(LocalDateTime.now());
            user.setPasswordExpirationDate(LocalDateTime.now().plusDays(90));
            userRepository.save(user);
        });
    }

    public void toggle2Fa(Long userId) {
        userRepository.findById(userId).ifPresent(user -> {
            user.setRequire2Fa(!user.isRequire2Fa());
            userRepository.save(user);
        });
    }

    public void setPasswordExpiration(Long userId, int daysToExpire) {
        userRepository.findById(userId).ifPresent(user -> {
            user.setPasswordExpirationDate(LocalDateTime.now().plusDays(daysToExpire));
            userRepository.save(user);
        });
    }

    public boolean sendSmsOrEmail2FaCode(String username) {
        // SMS or Email authentication logic simulated for demo purposes
        // Toggle switch allows enabling/disabling in system settings
        System.out.println("Simulating SMS/Email 2FA OTP code dispatch for user: " + username);
        return true;
    }
}
