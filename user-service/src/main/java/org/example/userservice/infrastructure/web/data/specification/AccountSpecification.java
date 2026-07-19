package org.example.userservice.infrastructure.web.data.specification;

import jakarta.persistence.criteria.Predicate;
import org.example.userservice.application.criteria.AccountSearchCriteria;
import org.example.userservice.infrastructure.web.entity.AccountEntity;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public final class AccountSpecification {

    private AccountSpecification() {
    }

    public static Specification<AccountEntity> fromCriteria(AccountSearchCriteria criteria) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (criteria.firstName() != null && !criteria.firstName().isBlank()) {
                predicates.add(cb.like(cb.lower(root.get("firstName")),
                        "%" + criteria.firstName().toLowerCase() + "%"));
            }

            if (criteria.lastName() != null && !criteria.lastName().isBlank()) {
                predicates.add(cb.like(cb.lower(root.get("lastName")),
                        "%" + criteria.lastName().toLowerCase() + "%"));
            }

            if (criteria.gender() != null) {
                predicates.add(cb.equal(root.get("gender"), criteria.gender()));
            }

            if (criteria.phoneNumber() != null && !criteria.phoneNumber().isBlank()) {
                predicates.add(cb.like(root.get("phoneNumber"), "%" + criteria.phoneNumber() + "%"));
            }

            if (criteria.createdFrom() != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("createdAt"), criteria.createdFrom()));
            }

            if (criteria.createdTo() != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("createdAt"), criteria.createdTo()));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
