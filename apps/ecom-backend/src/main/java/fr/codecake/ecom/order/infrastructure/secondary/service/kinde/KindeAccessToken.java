package fr.codecake.ecom.order.infrastructure.secondary.service.kinde;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Représente un jeton d'accès (Access Token) reçu depuis le service d'authentification Kinde.
 * Cette classe utilise `record` pour représenter un objet immuable avec des valeurs simples.
 *
 * @param accessToken Le jeton d'accès fourni par Kinde pour authentifier les requêtes de l'utilisateur.
 * @param tokenType Le type de jeton, généralement "Bearer".
 */
public record KindeAccessToken(
  @JsonProperty("access_token") String accessToken,
  @JsonProperty("token_type") String tokenType
) {
}
