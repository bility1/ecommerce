package fr.codecake.ecom.product.vo;

import fr.codecake.ecom.shared.error.domain.Assert;

public record ProductPrice(Double value) {

  public ProductPrice {
    Assert.field("value", value).min(0.1);
  }
}
