package fr.codecake.ecom.order.infrastructure.primary;

import fr.codecake.ecom.order.domain.user.aggregate.Authority;
import org.jilt.Builder;

import java.util.Set;
import java.util.stream.Collectors;

// Utilisation de @Builder pour générer automatiquement un constructeur avec un builder pattern.
@Builder
public record RestAuthority(String name) { // Déclaration d'un record (introduit en Java 14)

  /**
   * Méthode utilitaire qui transforme un ensemble d'objets Authority en un ensemble de noms sous forme de chaînes de caractères.
   *
   * @param authorities Un ensemble d'objets Authority.
   * @return Un ensemble de noms d'autorités sous forme de String.
   */
  public static Set<String> fromSet(Set<Authority> authorities) {
    return authorities == null ? Set.of() : authorities.stream() // Convertit le Set en Stream pour traitement fonctionnel
      .map(authority -> authority.getName().name()) // Extrait le nom de chaque autorité sous forme de String
      .collect(Collectors.toSet()); // Convertit le Stream en un Set<String>
  }
}
