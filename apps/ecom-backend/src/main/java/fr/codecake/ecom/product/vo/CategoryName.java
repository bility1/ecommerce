package fr.codecake.ecom.product.vo;

import fr.codecake.ecom.shared.error.domain.Assert;

public record CategoryName(String value) {

  public CategoryName {
    Assert.field("value", value).notNull().minLength(3);
  }
}
