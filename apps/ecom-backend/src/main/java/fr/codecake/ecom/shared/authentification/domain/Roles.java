package fr.codecake.ecom.shared.authentification.domain;

// Importation des classes nécessaires
import fr.codecake.ecom.shared.error.domain.Assert; //Importe la classe Assert.java, utilisée pour vérifier que les valeurs ne sont pas nulles.
import java.util.Collections; //Importe Collections, qui offre des méthodes utilitaires pour manipuler les collections (ex. rendre un ensemble immuable).
import java.util.Set; //Importe l'interface Set, qui représente un ensemble de rôles uniques.
import java.util.stream.Stream; //Importe Stream, qui est utilisé pour manipuler les rôles sous forme de flux.

// Définition d'un record immuable pour stocker les rôles d'un utilisateur
//Un record génère automatiquement :
//Un constructeur.
//Une méthode d'accès (roles()).
//Les méthodes equals(), hashCode() et toString()

public record Roles(Set<Role> roles) {

    // Constante représentant un objet "Roles" vide (évite les valeurs nulles)
    public static final Roles EMPTY = new Roles(Collections.emptySet());

    // Constructeur qui s'assure que l'ensemble des rôles est immuable
    public Roles(Set<Role> roles) {
        this.roles = roles == null ? Collections.emptySet() : Collections.unmodifiableSet(roles);
    }

    // Vérifie si l'utilisateur possède au moins un rôle
    public boolean hasRole() {
        return !roles.isEmpty();
    }

    // Vérifie si l'utilisateur possède un rôle spécifique
    public boolean hasRole(Role role) {
        Assert.notNull("role", role); // Vérifie que le rôle n'est pas null
        return roles.contains(role);
    }

    // Retourne un flux de rôles (permet d'effectuer des opérations fonctionnelles)
    public Stream<Role> stream() {
        return get().stream();
    }

    // Accesseur pour récupérer les rôles sous forme d'un ensemble immuable
    public Set<Role> get() {
        return roles();
    }
    // pour connaître le nombre de rôles
    public int size() {
        return roles.size();
    }

}
