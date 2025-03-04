package fr.codecake.ecom.product.domain.infrastructure.secondary.entity;

import fr.codecake.ecom.product.domain.aggregate.Category;
import fr.codecake.ecom.product.domain.aggregate.CategoryBuilder;
import fr.codecake.ecom.product.domain.vo.CategoryName;
import fr.codecake.ecom.product.domain.vo.PublicId;
import fr.codecake.ecom.shared.jpa.AbstractAuditingEntity;
import jakarta.persistence.*;
import org.jilt.Builder;

import java.util.Objects;
import java.util.Set;
import java.util.UUID;

/**
 * Entité JPA représentant une catégorie de produit dans la base de données.
 */
@Entity
@Table(name = "product_category") // Spécifie le nom de la table associée à cette entité
@Builder // Annotation pour générer un builder via Jilt
public class CategoryEntity extends AbstractAuditingEntity<Long> { // Hérite des propriétés d'audit

  /**
   * Clé primaire de la table, générée via une séquence.
   */
  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "categorySequence")
  @SequenceGenerator(name = "categorySequence", sequenceName = "product_category_sequence", allocationSize = 1)
  @Column(name = "id")
  private Long id;

  /**
   * Nom de la catégorie (champ obligatoire).
   */
  @Column(name = "name", nullable = false)
  private String name;

  /**
   * Identifiant public unique de la catégorie.
   */
  @Column(name = "public_id", unique = true, nullable = false)
  private UUID publicId;

  /**
   * Relation OneToMany avec les produits de cette catégorie.
   * La catégorie est référencée dans `ProductEntity` par la propriété `category`.
   */
  @OneToMany(mappedBy = "category")
  private Set<ProductEntity> products;

  /**
   * Constructeur par défaut nécessaire pour JPA.
   */
  public CategoryEntity() {
  }

  /**
   * Constructeur avec paramètres.
   * @param id Identifiant unique de la catégorie.
   * @param name Nom de la catégorie.
   * @param publicId Identifiant public UUID de la catégorie.
   * @param products Ensemble des produits associés à cette catégorie.
   */
  public CategoryEntity(Long id, String name, UUID publicId, Set<ProductEntity> products) {
    this.id = id;
    this.name = name;
    this.publicId = publicId;
    this.products = products;
  }

  /**
   * Convertit un objet `Category` (domaine) en `CategoryEntity` (persistance).
   * @param category Objet du domaine métier.
   * @return Entité correspondante pour la base de données.
   */
  public static CategoryEntity from(Category category) {
    CategoryEntityBuilder categoryEntityBuilder = CategoryEntityBuilder.categoryEntity();

    if (category.getDbId() != null) {
      categoryEntityBuilder.id(category.getDbId());
    }

    return categoryEntityBuilder
      .name(category.getName().value())
      .publicId(category.getPublicId().value())
      .build();
  }

  /**
   * Convertit une `CategoryEntity` (persistance) en `Category` (domaine).
   * @param categoryEntity Entité de la base de données.
   * @return Objet métier correspondant.
   */
  public static Category to(CategoryEntity categoryEntity) {
    return CategoryBuilder.category()
      .dbId(categoryEntity.getId())
      .name(new CategoryName(categoryEntity.getName()))
      .publicId(new PublicId(categoryEntity.getPublicId()))
      .build();
  }

  /**
   * Getter pour l'ID.
   */
  @Override
  public Long getId() {
    return id;
  }

  /**
   * Setter pour l'ID.
   */
  public void setId(Long id) {
    this.id = id;
  }

  /**
   * Getter pour le nom de la catégorie.
   */
  public String getName() {
    return name;
  }

  /**
   * Setter pour le nom de la catégorie.
   */
  public void setName(String name) {
    this.name = name;
  }

  /**
   * Getter pour l'identifiant public.
   */
  public UUID getPublicId() {
    return publicId;
  }

  /**
   * Setter pour l'identifiant public.
   */
  public void setPublicId(UUID publicId) {
    this.publicId = publicId;
  }

  /**
   * Getter pour la liste des produits associés à cette catégorie.
   */
  public Set<ProductEntity> getProducts() {
    return products;
  }

  /**
   * Setter pour la liste des produits associés.
   */
  public void setProducts(Set<ProductEntity> products) {
    this.products = products;
  }

  /**
   * Vérifie l'égalité entre deux `CategoryEntity`.
   * Deux catégories sont considérées comme égales si elles ont le même `publicId`.
   */
  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof CategoryEntity that)) return false;
    return Objects.equals(publicId, that.publicId);
  }

  /**
   * Calcule le hashCode basé sur `publicId`.
   */
  @Override
  public int hashCode() {
    return Objects.hashCode(publicId);
  }
}
