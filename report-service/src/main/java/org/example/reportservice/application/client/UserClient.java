package org.example.reportservice.application.client;

import org.example.reportservice.infrastructure.web.dto.AccountReportFilter;
import org.example.reportservice.infrastructure.web.dto.AccountReportResponsive;

import java.util.List;

public interface UserClient {
    List<AccountReportResponsive> getAccountReport(AccountReportFilter filter);
}
