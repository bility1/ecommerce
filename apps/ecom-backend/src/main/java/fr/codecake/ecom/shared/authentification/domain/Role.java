package fr.codecake.ecom.shared.authentification.domain;

// Importation des classes nécessaires
import fr.codecake.ecom.shared.error.domain.Assert;//Importe la classe Assert.java, utilisée pour valider que les valeurs ne sont pas vides ou nulles.
import java.util.Map; //Importe Map, utilisé pour stocker les rôles avec leurs clés associées.
import java.util.function.Function;//Importe Function, utilisé pour convertir un rôle en une clé lors de la construction de la Map.
import java.util.stream.Collectors;//Importe Collectors, utilisé pour collecter les éléments d'un flux dans une Map
import java.util.stream.Stream; //Importe Stream, utilisé pour traiter et transformer les rôles sous forme de flux.

// Définition de l'énumération des rôles
public enum Role {
  ADMIN,       // Rôle administrateur avec des privilèges élevés
  USER,        // Rôle utilisateur standard
  ANONYMOUS,   // Rôle pour les utilisateurs non authentifiés
  UNKNOWN;     // Rôle inconnu (utilisé par défaut si le rôle est invalide)

  // Préfixe pour la notation standard des rôles (ex: ROLE_ADMIN)
  private static final String PREFIX = "ROLE_";

  // Map contenant les rôles associées à leurs clés
  private static final Map<String, Role> ROLES = buildRoles();

  // Méthode privée pour construire une Map immuable des rôles
  private static Map<String, Role> buildRoles() {
    /*values()

    Retourne toutes les valeurs de l'énumération (ADMIN, USER, etc.).
    Stream.of(values())

    Transforme toutes les valeurs de l'énumération en un flux (Stream).
    collect(Collectors.toUnmodifiableMap(Role::key, Function.identity()))

    Construit une Map immuable où :
    La clé est Role::key (ex. "ROLE_ADMIN").
    La valeur est le rôle lui-même (Function.identity()). */

    return Stream.of(values()) // Transforme l'énumération en flux
      .collect(Collectors.toUnmodifiableMap(Role::key, Function.identity())); // Associe chaque rôle à sa clé
  }

  // Génère une clé unique pour chaque rôle (ex: ROLE_ADMIN)
  public String key() {
    return PREFIX + name();
  }

  // Méthode statique permettant de retrouver un rôle à partir d'une chaîne de texte
  public static Role from(String role) {
    Assert.notBlank("role", role); // Vérifie que le rôle fourni n'est pas vide ou null
    return ROLES.getOrDefault(role, UNKNOWN); // Retourne le rôle correspondant ou UNKNOWN si inexistant
  }
}
