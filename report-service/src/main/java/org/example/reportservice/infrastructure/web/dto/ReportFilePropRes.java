package org.example.reportservice.infrastructure.web.dto;

import org.springframework.http.MediaType;

public record ReportFilePropRes(
    byte[] fileBytes,
    MediaType mediaType,
    String fileName
) {
}