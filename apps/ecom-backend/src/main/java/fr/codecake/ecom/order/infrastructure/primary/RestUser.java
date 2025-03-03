package fr.codecake.ecom.order.infrastructure.primary;

import fr.codecake.ecom.order.domain.user.aggregate.User;
import org.jilt.Builder;

import java.util.Set;
import java.util.UUID;

import static org.hibernate.metamodel.model.domain.internal.PluralAttributeBuilder.build;

/**
 * Représentation d'un utilisateur pour l'API REST.
 * Ce record est immuable et encapsule les informations nécessaires pour un utilisateur dans l'interface publique.
 */
@Builder // Génère automatiquement un pattern Builder pour faciliter la création d'instances.
public record RestUser(
  UUID publicId,    // Identifiant public unique de l'utilisateur
  String firstName,
  String lastName,
  String email,
  String imageUrl,
  Set<String> authorities // Ensemble des rôles sous forme de chaînes de caractères
) {

  /**
   * Convertit un objet `User` du domaine en une représentation REST `RestUser`.
   *
   * @param user L'objet `User` à convertir.
   * @return Une instance de `RestUser` contenant les mêmes informations.
   */
  public static RestUser from(User user) {
    // Création d'un builder pour RestUser
    RestUserBuilder restUserBuilder = RestUserBuilder.restUser();

      if (user == null){
        throw new IllegalArgumentException("the user can't be null");
      }

    // Vérifie si l'utilisateur a une image, et si oui, l'ajoute au builder
    if (user.getImageUrl() != null) {
      restUserBuilder.imageUrl(user.getImageUrl().value());
    }

    // Construction de l'objet RestUser avec les valeurs extraites de l'entité User
    return restUserBuilder
      .email(user.getEmail().value()) // Adresse e-mail
      .firstName(user.getFirstname().value()) // Prénom
      .lastName(user.getLastname().value()) // Nom
      .publicId(user.getUserPublicId().value()) // Identifiant public
      .authorities(user.getAuthorities() != null ?
        RestAuthority.fromSet(user.getAuthorities()) : Set.of()) // Liste des rôles
      .build();
  }
}
