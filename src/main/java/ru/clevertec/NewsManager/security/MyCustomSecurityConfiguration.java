package ru.clevertec.NewsManager.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import ru.clevertec.NewsManager.dto.response.User;
import ru.clevertec.NewsManager.repository.UserManagementClient;


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
                                        .requestMatchers("/news").permitAll()
                                        .requestMatchers("/news/**").hasAnyRole("JOURNALIST","ADMIN")
                                        .requestMatchers("/comment/**").hasAnyRole("SUBSCRIBER","ADMIN")
                                        .requestMatchers("/user/**").hasRole("ADMIN")

                )
                .formLogin((formLogin) ->
                        formLogin
                                .loginProcessingUrl("/login")
                                .permitAll()
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
                                .logoutUrl("/logout") // URL для выхода из сессии
                                .logoutSuccessUrl("/login") // URL, на который перенаправляется после успешного выхода из сессии
                                .invalidateHttpSession(true) // инвалидировать HTTP-сессию пользователя после выхода
                                .deleteCookies("JSESSIONID") // удалить куки после выхода
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
}


