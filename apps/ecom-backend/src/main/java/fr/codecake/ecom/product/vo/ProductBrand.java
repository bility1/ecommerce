package fr.codecake.ecom.product.vo;

import fr.codecake.ecom.shared.error.domain.Assert;

public record ProductBrand(String value) {

  public ProductBrand{
    Assert.field("value", value).notNull().minLength(3);
  }

}
