package fr.codecake.ecom.product.domain.aggregate;

import fr.codecake.ecom.product.domain.vo.*;
import fr.codecake.ecom.shared.error.domain.Assert;
import org.jilt.Builder;

import java.util.List;
import java.util.UUID;

@Builder
public class Product {

  private final ProductBrand productBrand;
  private final ProductColor productColor;
  private final ProductDescription description;
  private final ProductName name;
  private final ProductPrice price;
  private final ProductSize size;
  private final Category category;
  private final List<Picture> pictures;
  private boolean featured;
  private PublicId publicId;
  private int nbInStock;

  public Product(ProductBrand brand, ProductColor color,
                 ProductDescription description, ProductName name, ProductPrice price, ProductSize size, Category category,
                 List<Picture> pictures, Long dbId, boolean featured, PublicId publicId, int nbInStock) {
    assertMandatoryFields(brand,color, description,name,price,size,category,pictures,featured,nbInStock);
    this.productBrand = brand;
    this.productColor = color;
    this.description = description;
    this.name = name;
    this.price = price;
    this.size = size;
    this.category = category;
    this.pictures = pictures;
    this.dbId = dbId;
    this.featured = featured;
    this.publicId = publicId;
    this.nbInStock = nbInStock;
  }

  private void assertMandatoryFields(
    ProductBrand brand, ProductColor color,
    ProductDescription description, ProductName name, ProductPrice price, ProductSize size, Category category,
    List<Picture> pictures, boolean featured, int nbInStock
  ){
    Assert.notNull("brand",brand);
    Assert.notNull("color",color);
    Assert.notNull("description",description);
    Assert.notNull("name",name);
    Assert.notNull("price",price);
    Assert.notNull("size",size);
    Assert.notNull("category",category);
    Assert.notNull("pictures",pictures);
    Assert.notNull("featured",featured);
    Assert.notNull("nbInStock",nbInStock);
  }
  private Long dbId;

  public ProductBrand getProductBrand() {
    return productBrand;
  }

  public ProductColor getColor() {
    return productColor;
  }

  public ProductDescription getDescription() {
    return description;
  }

  public ProductName getName() {
    return name;
  }


  public ProductPrice getPrice() {
    return price;
  }

  public ProductSize getSize() {
    return size;
  }

  public Category getCategory() {
    return category;
  }

  public List<Picture> getPictures() {
    return pictures;
  }

  public Long getDbId() {
    return dbId;
  }

  public boolean isFeatured() {
    return featured;
  }

  public PublicId getPublicId() {
    return publicId;
  }

  public void setFeatured(boolean featured) {
    this.featured = featured;
  }

  public int getNbInStock() {
    return nbInStock;
  }

  public void initDefaultFields() {
    this.publicId = new PublicId(UUID.randomUUID());
  }

}
