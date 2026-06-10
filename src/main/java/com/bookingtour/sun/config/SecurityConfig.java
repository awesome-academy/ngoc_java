package com.bookingtour.sun.config;

import com.bookingtour.sun.enums.UserRole;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
                .authorizeHttpRequests(auth -> auth

                        .requestMatchers(
                                "/login",
                                "/register",
                                "/css/**",
                                "/js/**",
                                "/images/**"
                        )
                        .permitAll()
                        // admin page
                        .requestMatchers("/admin/**")
                        .hasAnyRole(UserRole.ADMIN.name())
                        // client page
                        .requestMatchers("/")
                        .hasAnyRole(UserRole.ADMIN.name(), UserRole.USER.name())

                        .anyRequest()
                        .authenticated()
                )

                .formLogin(form -> form
                        .loginPage("/login")
                        .defaultSuccessUrl("/")
                        .permitAll()
                )

                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/login?logout")
                        .invalidateHttpSession(true)
                        .deleteCookies("JSESSIONID")
                        .permitAll()
                );

        return http.build();
    }
}