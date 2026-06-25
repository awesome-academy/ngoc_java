package com.bookingtour.sun.specification;

import com.bookingtour.sun.entity.Booking;
import com.bookingtour.sun.entity.Tour;
import com.bookingtour.sun.entity.User;
import com.bookingtour.sun.enums.BookingStatus;
import jakarta.persistence.criteria.Join;
import org.springframework.util.StringUtils;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;
import jakarta.persistence.criteria.Predicate;

public class AdminBookingSpecification {

    private AdminBookingSpecification() {
    }

    public static Specification<Booking> filter(
            String keyword,
            BookingStatus status) {

        return (root, query, cb) -> {

            List<Predicate> predicates = new ArrayList<>();

            if (StringUtils.hasText(keyword)) {

                Join<Booking, User> userJoin = root.join("user");

                Join<Booking, Tour> tourJoin = root.join("tour");

                String pattern = "%" + keyword.toLowerCase() + "%";

                predicates.add(
                        cb.or(
                                cb.like(cb.lower(userJoin.get("username")), pattern),
                                cb.like(cb.lower(userJoin.get("email")), pattern),
                                cb.like(cb.lower(tourJoin.get("name")), pattern)
                        )
                );
            }

            if (status != null) {
                predicates.add(cb.equal(root.get("status"), status));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
