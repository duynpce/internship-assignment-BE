package org.example.productservice.infrastructure.web.data.springdata;

import org.example.productservice.infrastructure.web.data.entity.TransactionEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.UUID;

// JpaSpecificationExecutor removed — complex queries are handled via MongoTemplate in the adapter
public interface SpringDataTransactionRepository extends MongoRepository<TransactionEntity, UUID> {
    List<TransactionEntity> findByProductId(UUID productId);
    List<TransactionEntity> findByContributorId(UUID contributorId);
}
