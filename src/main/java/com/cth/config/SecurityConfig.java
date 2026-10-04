package com.cth.config;

import com.cth.model.User;
import com.cth.repository.UserRepository;
import com.cth.service.LdapAuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import java.util.Collections;
import java.util.List;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Value("${cth.auth.provider:DATABASE}")
    private String authProvider;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private LdapAuthService ldapAuthService;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public UserDetailsService userDetailsService() {
        return username -> {
            User user = userRepository.findByUsername(username)
                    .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));

            if (user.isLocked()) {
                throw new RuntimeException("Account is locked");
            }
            if (!user.isEnabled()) {
                throw new RuntimeException("Account is disabled");
            }

            return org.springframework.security.core.userdetails.User.builder()
                    .username(user.getUsername())
                    .password(user.getPassword())
                    .roles(user.getRole().replace("ROLE_", ""))
                    .disabled(!user.isEnabled())
                    .accountLocked(user.isLocked())
                    .credentialsExpired(user.isPasswordExpired())
                    .build();
        };
    }

    @Bean
    public AuthenticationProvider customAuthenticationProvider() {
        return new AuthenticationProvider() {
            @Override
            public Authentication authenticate(Authentication authentication) throws AuthenticationException {
                String username = authentication.getName();
                String password = authentication.getCredentials().toString();

                if ("LDAP".equalsIgnoreCase(authProvider)) {
                    if (ldapAuthService.authenticateLdapUser(username, password)) {
                        return new UsernamePasswordAuthenticationToken(username, password,
                                List.of(new SimpleGrantedAuthority("ROLE_USER")));
                    } else {
                        throw new RuntimeException("Invalid LDAP credentials");
                    }
                } else {
                    User user = userRepository.findByUsername(username)
                            .orElseThrow(() -> new UsernameNotFoundException("User not found"));
                    if (user.isLocked()) {
                        throw new RuntimeException("Account is locked");
                    }
                    if (user.isPasswordExpired()) {
                        throw new RuntimeException("Password has expired. Reset required.");
                    }
                    if (passwordEncoder().matches(password, user.getPassword())) {
                        return new UsernamePasswordAuthenticationToken(username, password,
                                List.of(new SimpleGrantedAuthority(user.getRole())));
                    } else {
                        throw new RuntimeException("Invalid password");
                    }
                }
            }

            @Override
            public boolean supports(Class<?> authentication) {
                return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
            }
        };
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .headers(headers -> headers.frameOptions(frame -> frame.disable()))
            .authenticationProvider(customAuthenticationProvider())
            .authorizeHttpRequests(auth -> auth
                .requestMatchers(
                    "/login", "/register", "/css/**", "/js/**", "/images/**",
                    "/h2-console/**", "/swagger-ui/**", "/swagger-ui.html", "/v3/api-docs/**", "/api-docs/**",
                    "/actuator/**", "/ws-dashboard/**"
                ).permitAll()
                .anyRequest().authenticated()
            )
            .formLogin(form -> form
                .loginPage("/login")
                .defaultSuccessUrl("/dashboard", true)
                .failureUrl("/login?error=true")
                .permitAll()
            )
            .logout(logout -> logout
                .logoutUrl("/logout")
                .logoutSuccessUrl("/login?logout=true")
                .permitAll()
            );

        return http.build();
    }
}
