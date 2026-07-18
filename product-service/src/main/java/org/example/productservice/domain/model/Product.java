package org.example.productservice.domain.model;

import org.example.productservice.domain.constant.ProductCategory;

import java.math.BigDecimal;
import java.util.Map;
import java.util.UUID;

public class Product {
    private UUID id;
    private UUID contributorId;
    private String url;
    private String name;
    private BigDecimal price;
    private Integer quantity;
    private ProductCategory category;
    private Map<String, String> attributes;

    public Product(UUID id, UUID contributorId, String url, String name, BigDecimal price, Integer quantity, ProductCategory category, Map<String, String> attributes) {
        this.id = id;
        this.contributorId = contributorId;
        this.url = url;
        this.name = name;
        this.price = price;
        this.quantity = quantity;
        this.category = category;
        this.attributes = attributes;
    }

    public Product() {
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getContributorId() {
        return contributorId;
    }

    public void setContributorId(UUID contributorId) {
        this.contributorId = contributorId;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getName() {

        return name;
    }

    public void setName(String name) {
        if(name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Product name cannot be null or empty");
        }
        this.name = name;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        if(price == null || price.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Price cannot be null or negative");
        }
        this.price = price;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        if(quantity == null || quantity < 0) {
            throw new IllegalArgumentException("Quantity cannot be null or negative");
        }
        this.quantity = quantity;
    }

    public ProductCategory getCategory() {
        return category;
    }

    public void setCategory(ProductCategory category) {
        this.category = category;
    }

    public Map<String, String> getAttributes() {
        return attributes;
    }

    public void setAttributes(Map<String, String> attributes) {
        this.attributes = attributes;
    }

    public BigDecimal calculateSubtotal() {
        return price.multiply(BigDecimal.valueOf(quantity));
    }
}

