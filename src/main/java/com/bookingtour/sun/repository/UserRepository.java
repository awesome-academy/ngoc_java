package com.bookingtour.sun.repository;

import com.bookingtour.sun.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long>, JpaSpecificationExecutor<User> {
    Optional<User> findByEmail(String email);

    @Query("SELECT COUNT(u) FROM User u WHERE u.role = 'USER'")
    long countUsers();
}
