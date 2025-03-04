package fr.codecake.ecom.product.service;

import fr.codecake.ecom.product.aggregate.Product;
import fr.codecake.ecom.product.repository.ProductRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public class ProductCRUD {

  private final ProductRepository productRepository;


  public ProductCRUD(ProductRepository productRepository) {
    this.productRepository = productRepository;
  }

  public Product save(Product newProduct){
    newProduct.initDefaultFields();
    return productRepository.save(newProduct);
  }

  public Page<Product> findAll(Pageable pageable){
    return productRepository.findAll(pageable);
  }
}
