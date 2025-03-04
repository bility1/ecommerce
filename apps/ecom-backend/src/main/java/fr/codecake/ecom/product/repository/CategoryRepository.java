package fr.codecake.ecom.product.repository;

import fr.codecake.ecom.product.aggregate.Category;
import fr.codecake.ecom.product.vo.PublicId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CategoryRepository {

  Page<Category> findAll(Pageable pageable);
  int delete(PublicId publicId);

  Category save(Category categoryToCreate);
}
