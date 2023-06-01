package ru.clevertec.NewsManager.security;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import ru.clevertec.NewsManager.dto.response.User;
import ru.clevertec.NewsManager.repository.UserManagementClient;

import java.io.IOException;


@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class MyCustomSecurityConfiguration {

    private final UserManagementClient userManagementClient;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeRequests(
                        (authorizeRequests) ->
                                authorizeRequests
                                        .requestMatchers("/news","/users").permitAll()
                                        .requestMatchers("/news/**").hasAnyRole("JOURNALIST","ADMIN")
                                        .requestMatchers("/comment/**").hasAnyRole("SUBSCRIBER","ADMIN")
                                        .requestMatchers("/users/**","/users/admin").hasRole("ADMIN")

                )
                .formLogin((formLogin) ->
                        formLogin
                                .loginProcessingUrl("/login")
                                .permitAll()
                                .defaultSuccessUrl("/welcome", true)
                )
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
                );

        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(userDetailsService());
        authenticationProvider.setPasswordEncoder(passwordEncoder());
        http.authenticationProvider(authenticationProvider);

        return http.build();
    }

    @Bean
    public UserDetailsService userDetailsService() {
        return username -> {
            PasswordEncoder passwordEncoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
            User user = userManagementClient.read(username);
            System.out.println("user = " + user);
            if (user == null) {
                throw new UsernameNotFoundException("User not found: " + username);
            }

            return org.springframework.security.core.userdetails.User.builder()
                    .username(user.getUsername())
                    .password(passwordEncoder.encode(user.getPassword()))
                    .roles(user.getRole().getName())
                    .build();
        };
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    private void handleLoginSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws  IOException {
        // Получение имени пользователя из успешно аутентифицированного объекта Authentication
        String username = authentication.getName();

        // Отправка сообщения об успешной аутентификации и приветствие с именем пользователя
        String message = "Успешная аутентификация. Добро пожаловать, " + username + "!";
        response.getWriter().write(message);
    }
}


