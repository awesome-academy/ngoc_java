package com.bookingtour.sun.config;

import com.bookingtour.sun.enums.UserRole;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {
    private final OAuth2LoginSuccessHandler oAuth2LoginSuccessHandler;
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
                .authorizeHttpRequests(auth -> auth
                        // public pages
                        .requestMatchers(
                                "/tours",
                                "/login",
                                "/register",

                                "/css/**",
                                "/js/**",
                                "/images/**",
                                "/uploads/**",

                                "/login/oauth2/**",
                                "/oauth2/**"
                        )
                        .permitAll()
                        .requestMatchers(HttpMethod.GET,
                                "/",
                                "/tours/**"
                        ).permitAll()
                        // admin page
                        .requestMatchers("/admin/**")
                        .hasAnyRole(UserRole.ADMIN.name())
                        // client page
                        // booking phải login
                        .requestMatchers("/bookings/**")
                        .authenticated()

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
                )

                .oauth2Login(oauth -> oauth
                        .loginPage("/login")
                        .defaultSuccessUrl("/")
                        .successHandler(oAuth2LoginSuccessHandler)
                );

        return http.build();
    }
}
