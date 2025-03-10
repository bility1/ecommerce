package fr.codecake.ecom.product.domain.infrastructure.secondary.repository;

import fr.codecake.ecom.product.domain.aggregate.Category;
import fr.codecake.ecom.product.domain.infrastructure.secondary.entity.CategoryEntity;
import fr.codecake.ecom.product.domain.repository.CategoryRepository;
import fr.codecake.ecom.product.domain.vo.PublicId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
public class SpirngDataCategoryRepository implements CategoryRepository {

  private final JpaCategoryRepository  jpaCategoryRepository;

  public SpirngDataCategoryRepository(JpaCategoryRepository jpaCategoryRepository) {
    this.jpaCategoryRepository = jpaCategoryRepository;
  }

  @Override
  public Page<Category> findAll(Pageable pageable) {
    return jpaCategoryRepository.findAll(pageable).map(CategoryEntity::to);
  }

  @Override
  public int delete(PublicId publicId) {
    return jpaCategoryRepository.deleteByPublicId(publicId.value());
  }

  @Override
  public Category save(Category categoryToCreate) {
    CategoryEntity categoryToSave = CategoryEntity.from(categoryToCreate);
    CategoryEntity categorySaved = jpaCategoryRepository.save(categoryToSave);
    return CategoryEntity.to(categorySaved);
  }
}
