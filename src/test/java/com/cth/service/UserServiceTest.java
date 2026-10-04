package com.cth.service;

import com.cth.model.User;
import com.cth.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
public class UserServiceTest {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Test
    public void testUserRegistrationAndLocking() {
        User registered = userService.registerUser("jdoe", "password123", "John Doe", "jdoe@cth.com", "ROLE_USER");
        assertNotNull(registered.getId());
        assertEquals("jdoe", registered.getUsername());
        assertFalse(registered.isLocked());

        userService.lockUser(registered.getId());
        User lockedUser = userRepository.findById(registered.getId()).orElseThrow();
        assertTrue(lockedUser.isLocked());

        userService.unlockUser(registered.getId());
        User unlockedUser = userRepository.findById(registered.getId()).orElseThrow();
        assertFalse(unlockedUser.isLocked());
    }

    @Test
    public void testPasswordResetAndExpiration() {
        User registered = userService.registerUser("janedoe", "pass123", "Jane Doe", "jane@cth.com", "ROLE_USER");

        userService.setPasswordExpiration(registered.getId(), -1);
        User expiredUser = userRepository.findById(registered.getId()).orElseThrow();
        assertTrue(expiredUser.isPasswordExpired());

        userService.resetPassword(registered.getId(), "newPassword123!");
        User resetUser = userRepository.findById(registered.getId()).orElseThrow();
        assertFalse(resetUser.isPasswordExpired());
    }
}
