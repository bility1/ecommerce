package fr.codecake.ecom.order.domain.user.aggregate;

import fr.codecake.ecom.order.domain.user.vo.*;
import fr.codecake.ecom.shared.error.domain.Assert;
import org.jilt.Builder;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Classe représentant un utilisateur dans le domaine de l'application e-commerce.
 * Elle utilise le pattern Builder pour faciliter la création des instances.
 */
@Builder
public class User {

  // Définition des attributs de l'utilisateur
  private UserLastname lastname; // Nom de famille
  private UserFirstname firstname; // Prénom
  private UserEmail email; // Adresse e-mail
  private UserPublicId userPublicId; // Identifiant public unique de l'utilisateur
  private UserImageUrl imageUrl; // URL de l'image de profil
  private Instant lastModifiedDate; // Date de la dernière modification
  private Instant createdDate; // Date de création du compte
  private Set<Authority> authorities; // Ensemble des rôles/autorités de l'utilisateur
  private Long dbId; // Identifiant interne en base de données
  private UserAddress userAddress; // Adresse de l'utilisateur
  private Instant lastSeen; // Dernière connexion de l'utilisateur

  /**
   * Constructeur de la classe User avec tous les champs nécessaires.
   *
   * @param lastname Nom de famille
   * @param firstname Prénom
   * @param email Adresse email
   * @param userPublicId Identifiant public unique
   * @param imageUrl URL de l'image de profil
   * @param lastModifiedDate Date de la dernière modification
   * @param createdDate Date de création
   * @param authorities Ensemble des rôles de l'utilisateur
   * @param dbId Identifiant interne en base de données
   * @param userAddress Adresse de l'utilisateur
   * @param lastSeen Dernière connexion de l'utilisateur
   */
  public User(UserLastname lastname, UserFirstname firstname, UserEmail email, UserPublicId userPublicId,
              UserImageUrl imageUrl, Instant lastModifiedDate, Instant createdDate,
              Set<Authority> authorities, Long dbId, UserAddress userAddress, Instant lastSeen) {
    this.lastname = lastname;
    this.firstname = firstname;
    this.email = email;
    this.userPublicId = userPublicId;
    this.imageUrl = imageUrl;
    this.lastModifiedDate = lastModifiedDate;
    this.createdDate = createdDate;
    this.authorities = authorities;
    this.dbId = dbId;
    this.userAddress = userAddress;
    this.lastSeen = lastSeen;
  }

  /**
   * Vérifie que les champs obligatoires ne sont pas null.
   */
  private void assertMandatoryFields() {
    Assert.notNull("lastname", lastname);
    Assert.notNull("firstname", firstname);
    Assert.notNull("email", email);
    Assert.notNull("authorities", authorities);
  }

  /**
   * Met à jour certaines informations de l'utilisateur à partir d'un autre utilisateur.
   *
   * @param user L'utilisateur contenant les nouvelles données.
   */
  public void updateFromUser(User user) {
    this.email = user.email;
    this.imageUrl = user.imageUrl;
    this.firstname = user.firstname;
    this.lastname = user.lastname;
  }

  /**
   * Initialise les champs nécessaires lors de l'inscription d'un utilisateur.
   * Génère un identifiant public unique.
   */
  public void initFieldForSignup() {
    this.userPublicId = new UserPublicId(UUID.randomUUID());
  }

  /**
   * Crée une instance de User à partir des attributs d'un token d'authentification.
   *
   * @param attributes Map contenant les informations du token (email, prénom, nom, image, etc.).
   * @param rolesFromAccessToken Liste des rôles extraits du token.
   * @return Une instance de User remplie avec les informations du token.
   */
  public static User fromTokenAttributes(Map<String, Object> attributes, List<String> rolesFromAccessToken) {
    UserBuilder userBuilder = UserBuilder.user();

    if (attributes.containsKey("preferred_email")) {
      userBuilder.email(new UserEmail(attributes.get("preferred_email").toString()));
    }

    if (attributes.containsKey("last_name")) {
      userBuilder.lastname(new UserLastname(attributes.get("last_name").toString()));
    }

    if (attributes.containsKey("first_name")) {
      userBuilder.firstname(new UserFirstname(attributes.get("first_name").toString()));
    }

    if (attributes.containsKey("picture")) {
      userBuilder.imageUrl(new UserImageUrl(attributes.get("picture").toString()));
    }

    if (attributes.containsKey("last_signed_in")) {
      userBuilder.lastSeen(Instant.parse(attributes.get("last_signed_in").toString()));
    }

    // Transformation des rôles en objets Authority
    Set<Authority> authorities = rolesFromAccessToken.stream()
      .map(authority -> AuthorityBuilder.authority().name(new AuthorityName(authority)).build())
      .collect(Collectors.toSet());

    userBuilder.authorities(authorities);

    return userBuilder.build();
  }

  // Getters pour accéder aux attributs de la classe

  public UserLastname getLastname() {
    return lastname;
  }

  public UserFirstname getFirstname() {
    return firstname;
  }

  public UserEmail getEmail() {
    return email;
  }

  public UserPublicId getUserPublicId() {
    return userPublicId;
  }

  public UserImageUrl getImageUrl() {
    return imageUrl;
  }

  public Instant getLastModifiedDate() {
    return lastModifiedDate;
  }

  public Instant getCreatedDate() {
    return createdDate;
  }

  public Set<Authority> getAuthorities() {
    return authorities;
  }

  public Long getDbId() {
    return dbId;
  }

  public UserAddress getUserAddress() {
    return userAddress;
  }

  public Instant getLastSeen() {
    return lastSeen;
  }
}
