package fr.codecake.ecom.shared.authentication.infrastructure.primary;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;

@Configuration //Indique que cette classe est une classe de configuration Spring.
@EnableWebSecurity //Active Spring Security pour gérer l’authentification et l’autorisation.
public class SecurityConfiguration {

    @Bean //Indique que cette méthode retourne un bean Spring qui sera géré par le conteneu
    // HttpSecurity : Permet de configurer la sécurité des requêtes HTTP.
    //SecurityFilterChain : Définit la chaîne de filtres de sécurité utilisée par Spring Security.
    /*jwt(jwt -> jwt.jwtAuthenticationConverter()) :
    Active JWT (JSON Web Token) pour gérer l’authentification.
    Utilise un convertisseur d'authentification JWT. */

    public SecurityFilterChain configure (HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(authorize -> authorize
                .requestMatchers("/api/**").authenticated() // Toutes les requêtes vers /api/** doivent être authentifiées
                .anyRequest().permitAll() // Toutes les autres requêtes sont autorisées sans authentification
            )
            .csrf(AbstractHttpConfigurer::disable) // Désactive la protection CSRF (à utiliser avec précaution)
            .oauth2ResourceServer(oauth2 -> oauth2
                .jwt(jwt -> jwt.jwtAuthenticationConverter(new KindeJwtAuthenticationConverter())) // Configure le convertisseur JWT personnalisé
            );
     // Retourne la configuration de sécurité construite
        return http.build();
    }
}