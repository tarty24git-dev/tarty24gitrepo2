package com.cth.controller;

import com.cth.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping
    public String listUsers(Model model) {
        model.addAttribute("users", userService.getAllUsers());
        return "users";
    }

    @PostMapping("/lock")
    public String lockUser(@RequestParam("userId") Long userId) {
        userService.lockUser(userId);
        return "redirect:/users";
    }

    @PostMapping("/unlock")
    public String unlockUser(@RequestParam("userId") Long userId) {
        userService.unlockUser(userId);
        return "redirect:/users";
    }

    @PostMapping("/reset-password")
    public String resetPassword(@RequestParam("userId") Long userId,
                                @RequestParam("newPassword") String newPassword) {
        userService.resetPassword(userId, newPassword);
        return "redirect:/users";
    }

    @PostMapping("/toggle-2fa")
    public String toggle2Fa(@RequestParam("userId") Long userId) {
        userService.toggle2Fa(userId);
        return "redirect:/users";
    }

    @PostMapping("/expire-password")
    public String expirePassword(@RequestParam("userId") Long userId) {
        userService.setPasswordExpiration(userId, -1); // Expire immediately
        return "redirect:/users";
    }
}
