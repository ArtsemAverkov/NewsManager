package ru.clevertec.NewsManager.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import ru.clevertec.NewsManager.dto.user.User;
import ru.clevertec.NewsManager.repository.UserManagementClient;

import java.util.Objects;

import static org.springframework.security.config.Customizer.withDefaults;

/**
 The SecurityConfig class provides the security configuration for the NewsManager application.
 It defines the authentication provider, user details service, password encoder, and security filter chain.
 */

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final UserManagementClient userManagementClient;
    private final JwtTokenGenerator jwtTokenGenerator;

    /**
     Configures the security filter chain for handling HTTP security.
     @param http the HttpSecurity object to configure
     @return the configured security filter chain
     @throws Exception if an error occurs during configuration
     */

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(auth -> {
                    auth.requestMatchers("/news","/users/authenticate").permitAll();
                    auth.anyRequest().authenticated();
                })
                .oauth2Login(withDefaults())
                .formLogin(withDefaults())
                .sessionManagement((sessionManagement) ->
                        sessionManagement
                                .sessionConcurrency((sessionConcurrency) ->
                                        sessionConcurrency
                                                .maximumSessions(1)
                                                .expiredUrl("/login?expired")

                                )
                )
                .logout((logout) ->
                        logout
                                .logoutUrl("/logout")
                                .logoutSuccessUrl("/login")
                                .invalidateHttpSession(true)
                                .deleteCookies("JSESSIONID")
                )
                .csrf().disable();

        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(userDetailsService());
        authenticationProvider.setPasswordEncoder(passwordEncoder());
        http.authenticationProvider(authenticationProvider);

               return  http.build();
    }

    /**
     Creates and returns the UserDetailsService bean.
     @return the UserDetailsService bean
     */

    @Bean
    public UserDetailsService userDetailsService() {
        return username -> {
            PasswordEncoder passwordEncoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
            String token = jwtTokenGenerator.generateToken(username);
            User authenticateUser = userManagementClient.authenticateUser(token);

            if (Objects.nonNull(authenticateUser)) {
                String password = authenticateUser.getPassword();
                if (Objects.nonNull(password)) {
                    return org.springframework.security.core.userdetails.User.builder()
                            .username(authenticateUser.getUsername())
                            .password(passwordEncoder.encode(authenticateUser.getPassword()))
                            .roles(authenticateUser.getRole().getName())
                            .build();
                } else {
                    throw new RuntimeException("User password not found");
                }
            } else {
                throw new RuntimeException("User authentication failed");
            }
        };
    }

    /**
     Creates and returns the PasswordEncoder bean.
     @return the PasswordEncoder bean
     */

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }
}
