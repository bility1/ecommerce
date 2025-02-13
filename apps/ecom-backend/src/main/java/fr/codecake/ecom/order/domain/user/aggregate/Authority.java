package fr.codecake.ecom.order.domain.user.aggregate;

import fr.codecake.ecom.order.domain.user.vo.AuthorityName; // 🔹 Importation de la classe représentant un nom d'autorité.
import fr.codecake.ecom.shared.error.domain.Assert; // 🔹 Importation d'une classe utilitaire pour les validations.
import org.jilt.Builder; // 🔹 Utilisation de Jilt pour générer un builder automatiquement.

@Builder // 🔹 Annotation pour générer un pattern Builder avec Jilt.
public class Authority {

  private AuthorityName name; // 🔹 Champ privé représentant le nom de l'autorité.

  public Authority(AuthorityName authorityName) {
    Assert.notNull("name", authorityName); // 🔹 Vérifie que le nom d'autorité n'est pas `null` à la création.
    this.name = authorityName;
  }

  public AuthorityName getName() { // 🔹 Getter pour récupérer le nom de l'autorité.
    return name;
  }
}
