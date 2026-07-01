package com.bookingtour.sun.config;

import com.bookingtour.sun.entity.User;
import com.bookingtour.sun.enums.LoginProvider;
import com.bookingtour.sun.enums.UserRole;
import com.bookingtour.sun.enums.UserStatus;
import com.bookingtour.sun.repository.UserRepository;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class OAuth2LoginSuccessHandler
        extends SavedRequestAwareAuthenticationSuccessHandler {

    @Autowired
    UserRepository userRepository;

    @Override
    public void onAuthenticationSuccess(
            HttpServletRequest request,
            HttpServletResponse response,
            Authentication authentication)
            throws IOException, ServletException {

        OAuth2User user = (OAuth2User) authentication.getPrincipal();

        String email = user.getAttribute("email");

        String name = user.getAttribute("name");


        User dbUser = userRepository.findByEmail(email)
                        .orElseGet(() -> {
                            User u = new User();
                            u.setEmail(email);
                            u.setFullName(name);
                            String username = name != null ? name : email;
                            if (username != null && username.length() > 50) {
                                username = username.substring(0, 50);
                            }
                            u.setUsername(username);
                            u.setRole(UserRole.USER);
                            u.setStatus(UserStatus.ACTIVE);
                            u.setPasswordDigest("");
                            u.setLoginProvider(LoginProvider.GOOGLE);
                            return userRepository.save(u);
                        });

        super.onAuthenticationSuccess(
                request,
                response,
                authentication);
    }
}
