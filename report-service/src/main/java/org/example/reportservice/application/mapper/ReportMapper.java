package org.example.reportservice.application.mapper;


import org.example.reportservice.application.criteria.AccountSearchCriteria;
import org.example.reportservice.infrastructure.web.dto.AccountReportFilter;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;

public interface ReportMapper {

    AccountSearchCriteria toAccountSearchCriteria(AccountReportFilter filter);

}
