package fr.codecake.ecom.shared.authentification.domain;

// Importation de la classe Assert pour la validation des entrées
import fr.codecake.ecom.shared.error.domain.Assert;
// Importation de la classe StringUtils pour manipuler les chaînes de caractères
import org.apache.commons.lang3.StringUtils;

import java.util.Optional;

/**
 * Classe immuable représentant un nom d'utilisateur.
 * Utilise `record`, un type de Java permettant de stocker des données de manière optimisée.
 */
public record Username(String username) {

  /**
   * Constructeur de la classe `Username`.
   * Vérifie que le nom d'utilisateur est valide (non vide et longueur maximale de 100 caractères).
   *
   * @param username Le nom d'utilisateur.
   * @throws IllegalArgumentException si la validation échoue.
   */
  public Username {
    // Vérifie que le champ "username" n'est pas vide et respecte la longueur maximale de 100 caractères.
    Assert.field("username", username).notBlank().maxLength(100);
  }

  /**
   * Retourne la valeur du nom d'utilisateur.
   *
   * @return Le nom d'utilisateur sous forme de chaîne de caractères.
   */
  public String get() {
    return username();
  }

  /**
   * Méthode statique permettant de créer un objet `Username` uniquement si la chaîne est valide.
   *
   * @param username La chaîne de caractères représentant le nom d'utilisateur.
   * @return Un `Optional<Username>` contenant l'objet si la chaîne est valide, sinon un `Optional.empty()`.
   */
  public static Optional<Username> of(String username) {
    return Optional.ofNullable(username) // Vérifie que la valeur n'est pas `null`
        .filter(StringUtils::isNotBlank) // Vérifie que la chaîne n'est pas vide après suppression des espaces
        .map(Username::new); // Crée un objet `Username` si la validation passe
  }
}
