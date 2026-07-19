package org.example.productservice.infrastructure.web.data.springdata;

import org.example.productservice.domain.constant.ProductCategory;
import org.example.productservice.infrastructure.web.data.entity.ProductEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.UUID;

// JpaSpecificationExecutor removed — complex queries are handled via MongoTemplate in the adapter
public interface SpringDataProductRepository extends MongoRepository<ProductEntity, UUID> {
    List<ProductEntity> findByContributorId(UUID contributorId);
    List<ProductEntity> findByCategory(ProductCategory category);
}
