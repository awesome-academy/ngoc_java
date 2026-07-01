package com.bookingtour.sun.services;

import com.bookingtour.sun.dto.request.*;
import com.bookingtour.sun.dto.response.UserResponse;
import com.bookingtour.sun.entity.User;
import com.bookingtour.sun.exception.DuplicateResourceException;
import com.bookingtour.sun.exception.ResourceNotFoundException;
import com.bookingtour.sun.repository.UserRepository;
import com.bookingtour.sun.specification.UserSpecification;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.flywaydb.core.internal.util.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public PageResponse<UserResponse> searchUsers(UserSearchRequest request) {
        Pageable pageable = PageRequest.of(
                request.getPage(),
                request.getSize(),
                Sort.by("createdAt").descending()
        );

        Specification<User> specification = UserSpecification.filter(request.getKeyword(),
                request.getRole(),
                request.getStatus()
        );
        Page<User> page = userRepository.findAll(specification, pageable);

        List<UserResponse> users = page.getContent()
                .stream()
                .map(this::mapToResponse)
                .toList();

        return PageResponse.<UserResponse>builder()
                .content(users)
                .page(page.getNumber())
                .size(page.getSize())
                .totalElements(page.getTotalElements())
                .totalPages(page.getTotalPages())
                .build();
    }

    public void register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new DuplicateResourceException("Email already exists.");
        }
        User user = User.builder()
                .username(request.getUsername())
                .fullName(request.getFullName())
                .email(request.getEmail())
                .phone(request.getPhone())
                .passwordDigest(passwordEncoder.encode(request.getPassword()))
                .build();
        userRepository.save(user);
    }

    public void createUser(AdminCreateUserRequest request) {
        User user = User.builder()
                .username(request.getUsername())
                .fullName(request.getFullName())
                .email(request.getEmail())
                .phone(request.getPhone())
                .passwordDigest(passwordEncoder.encode(request.getPassword()))
                .role(request.getRole())
                .status(request.getStatus())
                .build();
        userRepository.save(user);
    }

    public AdminUpdateUserRequeset getForUpdate(Long id) {
        User user = userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));
        AdminUpdateUserRequeset request = new AdminUpdateUserRequeset();
        request.setId(id);
        request.setUsername(user.getUsername());
        request.setFullName(user.getFullName());
        request.setEmail(user.getEmail());
        request.setStatus(user.getStatus());
        request.setRole(user.getRole());
        request.setPhone(user.getPhone());
        return request;
    }

    @Transactional
    public void update(Long id, AdminUpdateUserRequeset request) {
        User user = userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));
        if (userRepository.existsByEmailAndIdNot(request.getEmail(), id)) {
            throw new DuplicateResourceException("Email already exists.");
        }
        user.setUsername(request.getUsername());
        user.setFullName(request.getFullName());
        user.setEmail(request.getEmail());
        user.setPhone(request.getPhone());
        user.setRole(request.getRole());
        user.setStatus(request.getStatus());
        if (StringUtils.hasText(request.getPassword())) {
            user.setPasswordDigest(
                    passwordEncoder.encode(request.getPassword())
            );
        }
        userRepository.save(user);
    }

    @Transactional
    public void deleteUser(Long id, String email) {
        User user = userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));
        User currentUser = userRepository.findByEmail(email).orElseThrow(() -> new ResourceNotFoundException("User not found with email: " + email));
        if (currentUser.getId().equals(id)) {
            throw new IllegalStateException("You cannot delete your own account.");
        }
        if (!user.getBookings().isEmpty()) {
            throw new IllegalStateException("Cannot delete user because they have bookings.");
        }
        if (!user.getReviews().isEmpty()) {
            throw new IllegalStateException("Cannot delete user because they have submitted reviews.");
        }

        userRepository.delete(user);
    }

    private UserResponse mapToResponse(User user) {
        return UserResponse.builder()
                .id(user.getId())
                .email(user.getEmail())
                .username(user.getUsername())
                .status(user.getStatus())
                .role(user.getRole())
                .build();
    }
}
