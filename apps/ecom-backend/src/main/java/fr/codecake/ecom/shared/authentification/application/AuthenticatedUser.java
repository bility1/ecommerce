package fr.codecake.ecom.shared.authentification.application;

// Importation des classes nécessaires
import com.nimbusds.jose.shaded.gson.internal.LinkedTreeMap;
import fr.codecake.ecom.shared.authentification.domain.Role;
import fr.codecake.ecom.shared.authentification.domain.Roles;
import fr.codecake.ecom.shared.authentification.domain.Username;
import fr.codecake.ecom.shared.error.domain.Assert;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Classe utilitaire pour récupérer les informations de l'utilisateur authentifié.
 * Elle ne peut pas être instanciée.
 */
public final class AuthenticatedUser {

    // Clé du champ contenant le nom d'utilisateur dans le token JWT
    public static final String PREFERRED_USERNAME = "email";

    // Constructeur privé pour empêcher l'instanciation de la classe
    private AuthenticatedUser() {}

    /**
     * Récupère le nom d'utilisateur de l'utilisateur authentifié.
     *
     * @return L'objet `Username.java`
     * @throws NotAuthenticatedUserException si l'utilisateur n'est pas authentifié.
     */
    public static Username username() {
        return optionalUsername().orElseThrow(NotAuthenticatedUserException::new);
    }

    /**
     * Récupère le nom d'utilisateur sous forme d'`Optional`.
     *
     * @return `Optional<Username.java>` si l'utilisateur est authentifié, sinon `Optional.empty()`
     */
    public static Optional<Username> optionalUsername() {
        return authentication()
                .map(AuthenticatedUser::readPrincipal)
                .flatMap(Username::of);
    }

    /**
     * Extrait le principal (identifiant de l'utilisateur) de l'objet `Authentication`.
     *
     * @param authentication L'objet `Authentication` à analyser.
     * @return Le nom d'utilisateur sous forme de `String`.
     * @throws UnknownAuthenticationException si l'authentification est inconnue.
     */
    public static String readPrincipal(Authentication authentication) {
        Assert.notNull("authentication", authentication);

        // Si l'authentification est basée sur un objet UserDetails (Spring Security classique)
        if (authentication.getPrincipal() instanceof UserDetails details) {
            return details.getUsername();
        }

        // Si l'utilisateur est authentifié via un JWT
        if (authentication instanceof JwtAuthenticationToken token) {
            return (String) token.getToken().getClaims().get(PREFERRED_USERNAME);
        }

        // Si l'utilisateur est authentifié via OpenID Connect (OIDC)
        if (authentication.getPrincipal() instanceof DefaultOidcUser oidcUser) {
            return (String) oidcUser.getAttributes().get(PREFERRED_USERNAME);
        }

        // Si l'utilisateur est simplement représenté par une chaîne de caractères
        if (authentication.getPrincipal() instanceof String principal) {
            return principal;
        }

        // Si aucun type d'authentification reconnu, lève une exception
        throw new UnknownAuthenticationException();
    }

    /**
     * Récupère les rôles de l'utilisateur authentifié.
     *
     * @return L'objet `Roles.java`, ou `Roles.java.EMPTY` si l'utilisateur n'est pas authentifié.
     */
    public static Roles roles() {
        return authentication().map(toRoles()).orElse(Roles.EMPTY);
    }

    /**
     * Convertit un objet `Authentication` en un objet `Roles.java`.
     *
     * @return Une fonction qui transforme `Authentication` en `Roles.java`.
     */
    private static Function<Authentication, Roles> toRoles() {
        return authentication ->
                new Roles(authentication.getAuthorities().stream()
                        .map(GrantedAuthority::getAuthority) // Extraction des rôles sous forme de chaîne
                        .map(Role::from) // Conversion en objets `Role`
                        .collect(Collectors.toSet())); // Transformation en `Set<Role>`
    }

    /**
     * Récupère les attributs du jeton JWT de l'utilisateur authentifié.
     *
     * @return Une map contenant les attributs du token.
     * @throws NotAuthenticatedUserException si l'utilisateur n'est pas authentifié.
     * @throws UnknownAuthenticationException si l'authentification est inconnue.
     */
    public static Map<String, Object> attributes() {
        Authentication token = authentication().orElseThrow(NotAuthenticatedUserException::new);

        // Si l'utilisateur est authentifié avec un JWT, retourne ses attributs
        if (token instanceof JwtAuthenticationToken jwtAuthenticationToken) {
            return jwtAuthenticationToken.getTokenAttributes();
        }

        // Si l'authentification n'est pas reconnue, on lève une exception
        throw new UnknownAuthenticationException();
    }

    /**
     * Récupère l'objet `Authentication` de l'utilisateur actuellement connecté.
     *
     * @return `Optional<Authentication>` contenant l'authentification, ou `Optional.empty()` si l'utilisateur n'est pas connecté.
     */
    private static Optional<Authentication> authentication() {
        return Optional.ofNullable(SecurityContextHolder.getContext().getAuthentication());
    }

    /**
     * Extrait les rôles de l'utilisateur à partir d'un token JWT.
     *
     * @param jwtToken Le token JWT contenant les informations de l'utilisateur.
     * @return Une liste contenant les rôles de l'utilisateur sous forme de `String`.
     */
    public static List<String> extractRolesFromToken(Jwt jwtToken) {
        // Récupère les rôles stockés dans l'attribut "roles" du JWT
        List<LinkedTreeMap<String, String>> realmAccess =
                (List<LinkedTreeMap<String, String>>) jwtToken.getClaims().get("roles");

        // Convertit les rôles extraits en liste de chaînes de caractères
        return realmAccess.stream()
                .map(roleTreeMap -> roleTreeMap.get("key")) // Récupération de la valeur associée à la clé "key"
                .toList();
    }
}
