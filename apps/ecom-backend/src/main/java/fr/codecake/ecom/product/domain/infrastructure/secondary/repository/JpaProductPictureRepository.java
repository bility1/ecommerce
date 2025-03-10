package fr.codecake.ecom.product.domain.infrastructure.secondary.repository;

import fr.codecake.ecom.product.domain.infrastructure.secondary.entity.PictureEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaProductPictureRepository extends JpaRepository<PictureEntity, Long> {
}
