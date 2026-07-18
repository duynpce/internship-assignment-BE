package org.example.productservice.infrastructure.web;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.productservice.application.command.CreateProductCommand;
import org.example.productservice.application.command.PageCommand;
import org.example.productservice.application.command.UpdateProductCommand;
import org.example.productservice.application.criteria.ProductSearchCriteria;
import org.example.productservice.application.mapper.ProductMapper;
import org.example.productservice.application.usecase.ProductUseCase;
import org.example.productservice.application.client.TokenGeneratorClient;
import org.example.productservice.domain.model.Product;
import org.example.productservice.infrastructure.web.dto.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductUseCase productUseCase;
    private final ProductMapper productMapper;
    private final TokenGeneratorClient tokenGeneratorClient;

    @PostMapping
    public ResponseEntity<ResponseDto<Void>> create(
            @Valid @RequestBody CreateProductRequest request,
            @CookieValue String accessToken) {
        UUID contributorId = tokenGeneratorClient.extractUserIdFromAccessToken(accessToken);
        CreateProductCommand command = productMapper.toCommand(request, contributorId);
        productUseCase.create(command);
        return new ResponseEntity<>(ResponseDto.success(null, "Product created successfully"), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseDto<ProductResponse>> findById(@PathVariable UUID id) {
        ProductResponse data = productMapper.toResponse(productUseCase.findById(id));
        return ResponseEntity.ok(ResponseDto.success(data));
    }

    /**
     * Filterable, paginated product search.
     * Example: GET /api/v1/products/search?name=shoes&category=FASHION&minPrice=10&page=0&limit=20
     */
    @GetMapping("/search")
    public ResponseEntity<ResponseDto<List<ProductResponse>>> search(
            @Valid @ModelAttribute ProductFilter filter) {
        ProductSearchCriteria criteria = productMapper.toCriteria(filter);
        PageCommand<Product> page = productUseCase.search(criteria);

        List<ProductResponse> data = page.getContent().stream()
                .map(productMapper::toResponse)
                .toList();

        MetaDto meta = MetaDto.builder()
                .totalItems(page.getTotalElements())
                .totalPages(page.getTotalPages())
                .paginationDto(new PaginationDto(filter.page(), filter.limit()))
                .build();

        return ResponseEntity.ok(ResponseDto.success(data, "Products fetched successfully", meta));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ResponseDto<ProductResponse>> update(
            @PathVariable UUID id,
            @Valid @RequestBody UpdateProductRequest request,
            @CookieValue String accessToken) {
        UUID senderId = tokenGeneratorClient.extractUserIdFromAccessToken(accessToken);
        UpdateProductCommand command = productMapper.toCommand(request, id, senderId);
        ProductResponse data = productMapper.toResponse(productUseCase.update(command));
        return ResponseEntity.ok(ResponseDto.success(data));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @PathVariable UUID id,
            @CookieValue String accessToken) {
        productUseCase.delete(id, accessToken);
        return ResponseEntity.noContent().build();
    }
}
