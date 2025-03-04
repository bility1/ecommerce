package fr.codecake.ecom.product.domain.infrastructure.secondary.entity;



import fr.codecake.ecom.product.domain.aggregate.Picture;
import fr.codecake.ecom.product.domain.aggregate.PictureBuilder;
import fr.codecake.ecom.shared.jpa.AbstractAuditingEntity;
import jakarta.persistence.*;
import org.jilt.Builder;

import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Entité JPA représentant une image d'un produit dans la base de données.
 */
@Entity
@Table(name = "product_picture") // Définit le nom de la table associée à cette entité.
@Builder // Utilisation de Jilt pour générer un builder.
public class PictureEntity extends AbstractAuditingEntity<Long> {

  /**
   * Clé primaire auto-générée via une séquence.
   */
  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "pictureSequence")
  @SequenceGenerator(name = "pictureSequence", sequenceName = "product_picture_sequence", allocationSize = 1)
  @Column(name = "id")
  private Long id;

  /**
   * Contenu binaire de l'image.
   * L'annotation @Lob indique que ce champ peut contenir de grandes données binaires.
   */
  @Lob
  @Column(name = "file", nullable = false)
  private byte[] file;

  /**
   * Type MIME du fichier image (ex: "image/png", "image/jpeg").
   */
  @Column(name = "file_content_type", nullable = false)
  private String mimeType;

  /**
   * Relation ManyToOne avec l'entité `ProductEntity`.
   * Un produit peut avoir plusieurs images, mais une image appartient à un seul produit.
   * `fetch = FetchType.LAZY` signifie que l'entité `ProductEntity` ne sera chargée que sur demande.
   */
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "product_fk", nullable = false) // Clé étrangère pointant vers `ProductEntity`
  private ProductEntity product;

  /**
   * Constructeur par défaut requis par JPA.
   */
  public PictureEntity() {
  }

  /**
   * Constructeur avec paramètres.
   */
  public PictureEntity(Long id, byte[] file, String mimeType, ProductEntity product) {
    this.id = id;
    this.file = file;
    this.mimeType = mimeType;
    this.product = product;
  }

  /**
   * Convertit un objet métier `Picture` en `PictureEntity` pour la persistance en base.
   */
  public static PictureEntity from(Picture picture) {
    return PictureEntityBuilder.pictureEntity()
      .file(picture.file())
      .mimeType(picture.mimeType())
      .build();
  }

  /**
   * Convertit une `PictureEntity` en objet métier `Picture`.
   */
  public static Picture to(PictureEntity pictureEntity) {
    return PictureBuilder.picture()
      .file(pictureEntity.getFile())
      .mimeType(pictureEntity.getMimeType())
      .build();
  }

  /**
   * Convertit une liste d'objets métier `Picture` en un ensemble d'`PictureEntity`.
   */
  public static Set<PictureEntity> from(List<Picture> pictures) {
    return pictures.stream().map(PictureEntity::from).collect(Collectors.toSet());
  }

  /**
   * Convertit un ensemble d'`PictureEntity` en une liste d'objets métier `Picture`.
   */
  public static List<Picture> to(Set<PictureEntity> pictureEntities) {
    return pictureEntities.stream().map(PictureEntity::to).collect(Collectors.toList());
  }

  // Getters et Setters

  @Override
  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public byte[] getFile() {
    return file;
  }

  public void setFile(byte[] file) {
    this.file = file;
  }

  public String getMimeType() {
    return mimeType;
  }

  public void setMimeType(String mimeType) {
    this.mimeType = mimeType;
  }

  public ProductEntity getProduct() {
    return product;
  }

  public void setProduct(ProductEntity product) {
    this.product = product;
  }

  /**
   * Vérifie l'égalité entre deux objets `PictureEntity`.
   * L'égalité est basée uniquement sur l'identifiant `id`.
   */
  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof PictureEntity that)) return false;
    return Objects.equals(id, that.id);
  }

  /**
   * Calcule le hashCode basé sur l'identifiant unique `id`.
   */
  @Override
  public int hashCode() {
    return Objects.hashCode(id);
  }
}
