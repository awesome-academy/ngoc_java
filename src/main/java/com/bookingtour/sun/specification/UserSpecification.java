package com.bookingtour.sun.specification;


import java.util.ArrayList;
import java.util.List;

import com.bookingtour.sun.entity.User;
import com.bookingtour.sun.enums.UserRole;
import com.bookingtour.sun.enums.UserStatus;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

public class UserSpecification {

    private UserSpecification() {
    }

    public static Specification<User> filter(
            String keyword,
            UserRole role,
            UserStatus status
    ) {

        return (root, query, cb) -> {

            List<Predicate> predicates = new ArrayList<>();

            if (StringUtils.hasText(keyword)) {
                String pattern = "%" + keyword.toLowerCase() + "%";
                predicates.add(
                        cb.or(
                                cb.like(cb.lower(root.get("username")), pattern),
                                cb.like(cb.lower(root.get("email")), pattern)
                        )
                );
            }

            if (role != null) {
                predicates.add(
                        cb.equal(
                                root.get("role"),
                                role
                        )
                );
            }

            if (status != null) {
                predicates.add(
                        cb.equal(
                                root.get("status"),
                                status
                        )
                );
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
