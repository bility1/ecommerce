package fr.codecake.ecom.shared.authentification.infrastructure.primary;

import fr.codecake.ecom.shared.authentification.application.AuthenticatedUser;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.lang.NonNull;

import java.util.Collection;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/*implements Converter<Jwt, AbstractAuthenticationToken> :
Indique que cette classe implémente l'interface Converter.
Converter<Jwt, AbstractAuthenticationToken> signifie que cette classe convertit un JWT en un jeton d'authentification (AbstractAuthenticationToken).
Utilité : Cette classe sert à extraire et transformer les informations du JWT en objets utilisables par Spring Security. */

public class KindeJwtAuthenticationConverter implements Converter<Jwt, AbstractAuthenticationToken> {

    @Override //Indique que cette méthode redéfinit (override) une méthode existante de l’interface Converter.
    //@NonNull : Empêche la valeur null d’être passée en argument (évite les erreurs NullPointerException).
    public AbstractAuthenticationToken convert(@NonNull Jwt source) {
        // Création d'un token d'authentification JWT
        return new JwtAuthenticationToken(source,
            Stream.concat(
                new JwtGrantedAuthoritiesConverter().convert(source).stream(), // Extraction des rôles classiques
                extractResourceRoles(source).stream() // Extraction des rôles personnalisés
            ).collect(Collectors.toSet()) // Suppression des doublons
        );
    }

    // Méthode pour extraire les rôles personnalisés du JWT
    private Collection<? extends GrantedAuthority> extractResourceRoles(Jwt jwt) {
        return AuthenticatedUser.extractRolesFromToken(jwt).stream() // Extraction des rôles du token
            .map(SimpleGrantedAuthority::new) // Conversion en objets GrantedAuthority
            .collect(Collectors.toSet()); // Stockage dans un ensemble (Set)
    }
}
