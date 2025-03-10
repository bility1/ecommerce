package fr.codecake.ecom.product.domain.infrastructure.secondary.repository;

import fr.codecake.ecom.product.domain.infrastructure.secondary.entity.ProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface JpaProductRepository extends JpaRepository <ProductEntity, Long> {

  int deleteByPublicId(UUID publicId);

  Optional<ProductEntity> findByPublicId(UUID publicID);
}
