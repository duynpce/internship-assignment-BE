package org.example.productservice.infrastructure.web.dto;

import jakarta.validation.constraints.*;
import org.example.productservice.domain.constant.ProductCategory;

import java.math.BigDecimal;
import java.util.Map;
import java.util.UUID;

public record CreateProductRequest(

        String url,

        @NotBlank(message = "Product name cannot be blank")
        String name,

        @NotNull(message = "Price cannot be null")
        @DecimalMin(value = "0.0", inclusive = true, message = "Price cannot be negative")
        BigDecimal price,

        @NotNull(message = "Quantity cannot be null")
        @Min(value = 0, message = "Quantity cannot be negative")
        Integer quantity,

        @NotNull(message = "Category cannot be null")
        ProductCategory category,

        Map<String, String> attributes
) {}
