package org.example.productservice.application.mapper;

import org.example.productservice.application.command.CreateProductCommand;
import org.example.productservice.application.command.UpdateProductCommand;
import org.example.productservice.application.criteria.ProductSearchCriteria;
import org.example.productservice.domain.model.Product;
import org.example.productservice.infrastructure.web.data.entity.ProductEntity;
import org.example.productservice.infrastructure.web.dto.CreateProductRequest;
import org.example.productservice.infrastructure.web.dto.ProductFilter;
import org.example.productservice.infrastructure.web.dto.ProductResponse;
import org.example.productservice.infrastructure.web.dto.UpdateProductRequest;

import java.util.UUID;

public interface ProductMapper {
    Product toDomain(ProductEntity entity);
    Product toDomain(CreateProductCommand command);
    ProductEntity toEntity(Product product);
    void updateFromCommand(UpdateProductCommand command, Product product);

    CreateProductCommand toCommand(CreateProductRequest request, UUID contributorId);
    UpdateProductCommand toCommand(UpdateProductRequest request, UUID id, UUID senderId);

    ProductSearchCriteria toCriteria(ProductFilter filter);

    ProductResponse toResponse(Product product);
}
