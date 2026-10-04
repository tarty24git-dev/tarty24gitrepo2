package com.cth.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "cth_users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String username;

    @Column(nullable = false)
    private String password;

    private String fullName;

    private String email;

    private String phoneNumber;

    private String role = "ROLE_USER"; // ROLE_ADMIN, ROLE_USER

    private boolean locked = false;

    private boolean enabled = true;

    private LocalDateTime passwordLastChanged = LocalDateTime.now();

    private LocalDateTime passwordExpirationDate = LocalDateTime.now().plusDays(90);

    private boolean require2Fa = false;

    public User() {}

    public User(String username, String password, String fullName, String email, String role) {
        this.username = username;
        this.password = password;
        this.fullName = fullName;
        this.email = email;
        this.role = role;
        this.locked = false;
        this.enabled = true;
        this.passwordLastChanged = LocalDateTime.now();
        this.passwordExpirationDate = LocalDateTime.now().plusDays(90);
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPhoneNumber() { return phoneNumber; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }

    public boolean isLocked() { return locked; }
    public void setLocked(boolean locked) { this.locked = locked; }

    public boolean isEnabled() { return enabled; }
    public void setEnabled(boolean enabled) { this.enabled = enabled; }

    public LocalDateTime getPasswordLastChanged() { return passwordLastChanged; }
    public void setPasswordLastChanged(LocalDateTime passwordLastChanged) { this.passwordLastChanged = passwordLastChanged; }

    public LocalDateTime getPasswordExpirationDate() { return passwordExpirationDate; }
    public void setPasswordExpirationDate(LocalDateTime passwordExpirationDate) { this.passwordExpirationDate = passwordExpirationDate; }

    public boolean isRequire2Fa() { return require2Fa; }
    public void setRequire2Fa(boolean require2Fa) { this.require2Fa = require2Fa; }

    public boolean isPasswordExpired() {
        return passwordExpirationDate != null && LocalDateTime.now().isAfter(passwordExpirationDate);
    }
}
