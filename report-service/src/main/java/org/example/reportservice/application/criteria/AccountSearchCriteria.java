package org.example.reportservice.application.criteria;


import org.example.reportservice.domain.constant.ExportFileName;
import org.example.reportservice.domain.constant.Gender;

import java.time.Instant;

/**
 * Filter-only fields. Pagination/sorting is handled separately via Spring's Pageable.
 */
public record AccountSearchCriteria(
        String firstName,
        String lastName,
        Gender gender,
        String phoneNumber,
        Instant createdFrom,
        Instant createdTo,
        Integer page,
        Integer limit,
        ExportFileName exportFileName
) {
}
