package org.example.productservice.infrastructure.web.dto;

import jakarta.validation.constraints.*;
import org.example.productservice.domain.constant.ProductCategory;

import java.math.BigDecimal;
import java.util.Map;
import java.util.UUID;

public record UpdateProductRequest(
        @NotNull(message = "Product ID cannot be null")
        UUID id,

        String name,

        @DecimalMin(value = "0.0", message = "Price cannot be negative")
        BigDecimal price,

        @Min(value = 0, message = "Quantity cannot be negative")
        Integer quantity,

        ProductCategory category,

        Map<String, String> attributes
) {}
