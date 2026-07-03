package org.example.reportservice.application.service;

import jakarta.annotation.PostConstruct;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lombok.RequiredArgsConstructor;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.export.ooxml.JRXlsxExporter;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import net.sf.jasperreports.export.SimpleXlsxReportConfiguration;
import net.sf.jasperreports.pdf.JRPdfExporter;
import org.example.reportservice.application.client.UserClient;
import org.example.reportservice.application.usecase.JasperReportUseCase;
import org.example.reportservice.infrastructure.web.dto.AccountReportFilter;
import org.example.reportservice.infrastructure.web.dto.AccountReportResponsive;
import org.example.reportservice.infrastructure.web.dto.ReportFilePropRes;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class JasperReportService implements JasperReportUseCase {

    private static final String COMPILED_REPORT_PATH = "report/account_report.jrxml";
    private final UserClient userClient;
    private JasperReport compiledReport;


    @PostConstruct
    void loadAndCompileReport() throws JRException {
        try (InputStream is = new ClassPathResource(COMPILED_REPORT_PATH).getInputStream()) {
            this.compiledReport = JasperCompileManager.compileReport(is);
        } catch (IOException e) {
            throw new JRException("Error loading report source file", e);
        }
    }

    @Override
    public ReportFilePropRes generateAccountReport(AccountReportFilter filter) throws JRException {

        List<AccountReportResponsive> data = userClient.getAccountReport(filter);

        return switch (filter.exportFileName()) {
            case PDF -> generateAccountReportPdf(data);
            case XLSX -> generateAccountReportXlsx(data);
        };
    }

    private ReportFilePropRes generateAccountReportPdf(List<AccountReportResponsive> data)
            throws JRException {
        JasperPrint print = fill(data);
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        JRPdfExporter exporter = new JRPdfExporter();
        exporter.setExporterInput(new SimpleExporterInput(print));
        exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(out));
        exporter.exportReport();

        return new ReportFilePropRes(out.toByteArray(), MediaType.APPLICATION_PDF,"account_report.pdf");
    }

    private ReportFilePropRes generateAccountReportXlsx(List<AccountReportResponsive> data)
            throws JRException {
        JasperPrint print = fill(data);
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        JRXlsxExporter exporter = new JRXlsxExporter();
        exporter.setExporterInput(new SimpleExporterInput(print));
        exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(out));

        SimpleXlsxReportConfiguration config = new SimpleXlsxReportConfiguration();
        config.setOnePagePerSheet(false);
        config.setRemoveEmptySpaceBetweenRows(true);
        config.setDetectCellType(true);
        exporter.setConfiguration(config);
        exporter.exportReport();

        return new ReportFilePropRes(out.toByteArray(), MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")
                , "account_report.xlsx");
    }

    private JasperPrint fill(List<AccountReportResponsive> data) throws JRException {
        JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(data);
        Map<String, Object> params = new HashMap<>();
        params.put("ReportTitle", "Account Report");
        return JasperFillManager.fillReport(compiledReport, params, dataSource);
    }
}