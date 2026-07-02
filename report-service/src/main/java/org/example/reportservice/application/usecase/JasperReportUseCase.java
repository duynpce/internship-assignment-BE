package org.example.reportservice.application.usecase;

import net.sf.jasperreports.engine.JRException;
import org.example.reportservice.infrastructure.web.dto.AccountReportFilter;
import org.example.reportservice.infrastructure.web.dto.ReportFilePropRes;

public interface JasperReportUseCase {

    ReportFilePropRes generateAccountReport(AccountReportFilter filter) throws JRException;
}