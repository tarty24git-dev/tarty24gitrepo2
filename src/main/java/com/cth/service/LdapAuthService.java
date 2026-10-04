package com.cth.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class LdapAuthService {

    @Value("${cth.auth.provider:DATABASE}")
    private String authProvider;

    public boolean isLdapEnabled() {
        return "LDAP".equalsIgnoreCase(authProvider);
    }

    public boolean authenticateLdapUser(String username, String password) {
        if (!isLdapEnabled()) {
            return false;
        }
        // Simulated Windows Active Directory LDAP validation for demo environment
        // Accepts any domain user credential in demo mode if password length > 3
        return username != null && password != null && password.length() >= 4;
    }
}
