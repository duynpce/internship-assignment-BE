package org.example.productservice.application.command;

import org.example.productservice.domain.constant.ProductCategory;

import java.math.BigDecimal;
import java.util.Map;
import java.util.UUID;

public record CreateProductCommand(
        UUID contributorId,
        String url,
        String name,
        BigDecimal price,
        Integer quantity,
        ProductCategory category,
        Map<String, String> attributes
) {}
