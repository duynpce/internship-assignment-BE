package org.example.productservice.infrastructure.web.data.entity;

import lombok.*;
import org.example.productservice.domain.constant.ProductCategory;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Map;
import java.util.UUID;

@Document(collection = "products")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductEntity {

    @Id
    @Builder.Default
    private UUID id = UUID.randomUUID();

    @Indexed
    private UUID contributorId;

    private String url;

    private String name;

    private BigDecimal price;

    private Integer quantity;

    private ProductCategory category;

    // MongoDB natively stores nested objects/maps — no special type hint needed
    private Map<String, String> attributes;

    @CreatedDate
    private Instant createdAt;

    @LastModifiedDate
    private Instant updatedAt;
}
