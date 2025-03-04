package fr.codecake.ecom.product.repository;

import fr.codecake.ecom.product.aggregate.Product;
import fr.codecake.ecom.product.vo.PublicId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ProductRepository {

  Product save(Product productToCreate);

  Page<Product> findAll(Pageable pageable);

  int delete(PublicId publicId);
}
