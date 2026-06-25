package com.bookingtour.sun.services;

import com.bookingtour.sun.dto.request.PageResponse;
import com.bookingtour.sun.dto.request.UserSearchRequest;
import com.bookingtour.sun.dto.response.UserResponse;
import com.bookingtour.sun.entity.User;
import com.bookingtour.sun.repository.UserRepository;
import com.bookingtour.sun.specification.UserSpecification;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

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
