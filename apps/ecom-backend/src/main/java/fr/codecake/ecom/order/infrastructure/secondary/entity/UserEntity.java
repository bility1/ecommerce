package fr.codecake.ecom.order.infrastructure.secondary.entity;

import fr.codecake.ecom.order.domain.user.aggregate.User;
import fr.codecake.ecom.order.domain.user.aggregate.UserBuilder;
import fr.codecake.ecom.order.domain.user.vo.*;
import fr.codecake.ecom.shared.jpa.AbstractAuditingEntity;
import jakarta.persistence.*;
import org.jilt.Builder;

import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

// Déclare cette classe comme une entité JPA et la lie à la table "ecommerce_user"
@Entity
@Table(name = "ecommerce_user")
@Builder // Génère automatiquement un Builder pour cette classe avec la bibliothèque Jilt
public class UserEntity extends AbstractAuditingEntity<Long> { // Hérite des fonctionnalités d'audit

  // Clé primaire auto-générée via une séquence PostgreSQL
  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "userSequenceGenerator")
  @SequenceGenerator(name = "userSequenceGenerator", sequenceName = "user_sequence", allocationSize = 1)
  @Column(name = "id")
  private Long id;

  // Informations personnelles de l'utilisateur
  @Column(name = "last_name")
  private String lastName;

  @Column(name = "first_name")
  private String firstName;

  @Column(name = "email")
  private String email;

  @Column(name = "image_url")
  private String imageURL;

  @Column(name = "public_id")
  private UUID publicId; // Identifiant unique public (UUID)

  // Adresse de l'utilisateur
  @Column(name = "address_street")
  private String addressStreet;

  @Column(name = "address_city")
  private String addressCity;

  @Column(name = "address_zip_code")
  private String addressZipCode;

  @Column(name = "address_country")
  private String addressCountry;

  // Dernière activité de l'utilisateur
  @Column(name = "last_seen")
  private Instant lastSeen;

  // Relation Many-to-Many avec AuthorityEntity (rôles utilisateur)
  @ManyToMany(cascade = CascadeType.REMOVE)
  @JoinTable(
    name = "user_authority",
    joinColumns = {@JoinColumn(name = "user_id", referencedColumnName = "id")},
    inverseJoinColumns = {@JoinColumn(name = "authority_name", referencedColumnName = "name")}
  )
  private Set<AuthorityEntity> authorities = new HashSet<>();

  // Constructeur par défaut requis par JPA
  public UserEntity() { }

  // Constructeur permettant d'initialiser un utilisateur avec toutes ses informations
  public UserEntity(Long id, String lastName, String firstName, String email, String imageURL, UUID publicId,
                    String addressStreet, String addressCity, String addressZipCode, String addressCountry,
                    Instant lastSeen, Set<AuthorityEntity> authorities) {
    this.id = id;
    this.lastName = lastName;
    this.firstName = firstName;
    this.email = email;
    this.imageURL = imageURL;
    this.publicId = publicId;
    this.addressStreet = addressStreet;
    this.addressCity = addressCity;
    this.addressZipCode = addressZipCode;
    this.addressCountry = addressCountry;
    this.lastSeen = lastSeen;
    this.authorities = authorities;
  }

  // Met à jour les informations de l'utilisateur à partir de l'objet métier User
  public void updateFromUser(User user) {
    this.email = user.getEmail().value();
    this.lastName = user.getLastname().value();
    this.firstName = user.getFirstname().value();
    this.imageURL = user.getImageUrl().value();
    this.lastSeen = user.getLastSeen();
  }

  // Conversion d'un objet User (métier) vers UserEntity (base de données)
  public static UserEntity from(User user) {
    UserEntityBuilder userEntityBuilder = UserEntityBuilder.userEntity();

    if (user.getImageUrl() != null) {
      userEntityBuilder.imageURL(user.getImageUrl().value());
    }

    if (user.getUserPublicId() != null) {
      userEntityBuilder.publicId(user.getUserPublicId().value());
    }

    if (user.getUserAddress() != null) {
      userEntityBuilder.addressCity(user.getUserAddress().city());
      userEntityBuilder.addressCountry(user.getUserAddress().country());
      userEntityBuilder.addressZipCode(user.getUserAddress().zipCode());
      userEntityBuilder.addressStreet(user.getUserAddress().street());
    }

    return userEntityBuilder
      .authorities(AuthorityEntity.from(user.getAuthorities()))
      .email(user.getEmail().value())
      .firstName(user.getFirstname().value())
      .lastName(user.getLastname().value())
      .lastSeen(user.getLastSeen())
      .id(user.getDbId())
      .build();
  }

  // Conversion d'un objet UserEntity vers User (domaine métier)
  public static User toDomain(UserEntity userEntity) {
    UserBuilder userBuilder = UserBuilder.user();

    if (userEntity.getImageURL() != null) {
      userBuilder.imageUrl(new UserImageUrl(userEntity.getImageURL()));
    }

    if (userEntity.getAddressStreet() != null) {
      userBuilder.userAddress(
        UserAddressBuilder.userAddress()
          .city(userEntity.getAddressCity())
          .country(userEntity.getAddressCountry())
          .zipCode(userEntity.getAddressZipCode())
          .street(userEntity.getAddressStreet())
          .build()
      );
    }

    return userBuilder
      .email(new UserEmail(userEntity.getEmail()))
      .lastname(new UserLastname(userEntity.getLastName()))
      .firstname(new UserFirstname(userEntity.getFirstName()))
      .authorities(AuthorityEntity.toDomain(userEntity.getAuthorities()))
      .userPublicId(new UserPublicId(userEntity.getPublicId()))
      .lastModifiedDate(userEntity.getLastModifiedDate())
      .createdDate(userEntity.getCreatedDate())
      .dbId(userEntity.getId())
      .build();
  }
  // Getters et setters
  // ...
  public void setId(Long id) {
    this.id = id;
  }

  public String getLastName() {
    return lastName;
  }

  public void setLastName(String lastName) {
    this.lastName = lastName;
  }

  public String getFirstName() {
    return firstName;
  }

  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }
  private String getImageURL() {
    return imageURL;
  }
  public void setAddressZipCode(String addressZipCode) {
    this.addressZipCode = addressZipCode;
  }

  public String getAddressCountry() {
    return addressCountry;
  }

  public void setAddressCountry(String addressCountry) {
    this.addressCountry = addressCountry;
  }

  public Instant getLastSeen() {
    return lastSeen;
  }

  public void setLastSeen(Instant lastSeen) {
    this.lastSeen = lastSeen;
  }

  public Set<AuthorityEntity> getAuthorities() {
    return authorities;
  }

  public void setAuthorities(Set<AuthorityEntity> authorities) {
    this.authorities = authorities;
  }

  public void setPublicId(UUID publicId) {
    this.publicId = publicId;
  }

  public String getAddressStreet() {
    return addressStreet;
  }

  public void setAddressStreet(String addressStreet) {
    this.addressStreet = addressStreet;
  }

  public String getAddressCity() {
    return addressCity;
  }

  public void setAddressCity(String addressCity) {
    this.addressCity = addressCity;
  }

  public String getAddressZipCode() {
    return addressZipCode;
  }
  private String getEmail() {
    return email;
  }

  // Méthodes de conversion de liste
  public static Set<UserEntity> from(List<User> users) {
    return users.stream().map(UserEntity::from).collect(Collectors.toSet());
  }

  public static Set<User> toDomain(List<UserEntity> users) {
    return users.stream().map(UserEntity::toDomain).collect(Collectors.toSet());
  }
  public void setImageURL(String imageURL) {
    this.imageURL = imageURL;
  }

  public UUID getPublicId() {
    return publicId;
  }


  // Comparaison entre objets UserEntity basée sur publicId (UUID)
  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof UserEntity that)) return false;
    return Objects.equals(publicId, that.publicId);
  }

  // Génère un hash unique basé sur publicId (UUID)
  @Override
  public int hashCode() {
    return Objects.hashCode(publicId);
  }

  @Override
  public Long getId() {
    return id;
  }
}
