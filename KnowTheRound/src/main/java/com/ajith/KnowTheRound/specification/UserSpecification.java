package com.ajith.KnowTheRound.specification;

import com.ajith.KnowTheRound.model.User;
import org.springframework.data.jpa.domain.Specification;

public class UserSpecification {

    public static Specification<User> searchUsers(String search) {

        return (root, query, criteriaBuilder) -> {

            if (search == null || search.isBlank()) {
                return null;
            }

            String keyword = "%" + search.toLowerCase() + "%";

            return criteriaBuilder.or(
                    criteriaBuilder.like(
                            criteriaBuilder.lower(root.get("name")),
                            keyword
                    ),
                    criteriaBuilder.like(
                            criteriaBuilder.lower(root.get("email")),
                            keyword
                    )
            );
        };
    }
}