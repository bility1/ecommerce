package fr.codecake.ecom.order.infrastructure.secondary.service.kinde;

import org.apache.hc.core5.http.ContentType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.net.URI;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Map;
import java.util.Optional;

@Service // Marque cette classe comme un service Spring, ce qui permet son injection dans d'autres classes
public class KindeService {

  // Injection des valeurs de configuration depuis application.properties ou application.yml
  @Value("${application.kinde.api}")
  private String apiUrl;

  @Value("${application.kinde.client-id}")
  private String clientId;

  @Value("${application.kinde.client-secret}")
  private String clientSecret;

  @Value("${application.kinde.audience}")
  private String audience;

  // Création d'un logger pour enregistrer les messages d'information et d'erreur
  private static final Logger log = LoggerFactory.getLogger(KindeService.class);

  // Initialisation d'un client HTTP pour envoyer des requêtes API
  private final RestClient restClient = RestClient.builder()
    .requestFactory(new HttpComponentsClientHttpRequestFactory()) // Utilisation de HttpClient
    .baseUrl(apiUrl) // Définit l'URL de base de l'API Kinde
    .build();

  /**
   * Méthode pour récupérer un token d'accès en effectuant une requête POST vers l'API Kinde.
   *
   * @return Optional<String> contenant le token JWT, ou empty() si la requête échoue.
   */
  private Optional<String> getToken() {
    try {
      // Construction de la requête POST pour obtenir un token d'accès
      ResponseEntity<KindeAccessToken> accessToken =
        restClient.post()
          .uri(URI.create("/oauth/token")) // Endpoint pour récupérer le token
          .body("grant_type=client_credentials&audience=" + URLEncoder.encode(audience, StandardCharsets.UTF_8)) // Paramètres OAuth
          .accept(MediaType.APPLICATION_JSON) // Accepte du JSON en réponse
          .contentType(MediaType.APPLICATION_FORM_URLENCODED) // Spécifie le format de l'envoi
          .header("Authorization", "Basic " + Base64.getEncoder().encodeToString(
            (clientId + ":" + clientSecret).getBytes(StandardCharsets.UTF_8) // Encodage des credentials en Base64
          ))
          .header("Content-Type", ContentType.APPLICATION_FORM_URLENCODED.getMimeType())
          .retrieve()
          .toEntity(KindeAccessToken.class); // Récupération de la réponse sous forme d'objet KindeAccessToken

      // Retourne le token s'il est présent dans la réponse
      return Optional.of(accessToken.getBody().accessToken());
    } catch (Exception e) {
      // Log en cas d'échec de la requête
      log.error("Error while getting token", e);
      return Optional.empty();
    }
  }

  /**
   * Récupère les informations d'un utilisateur donné en appelant l'API Kinde avec un token d'accès.
   *
   * @param userId L'ID de l'utilisateur dont on veut récupérer les informations
   * @return Un Map contenant les informations de l'utilisateur sous forme clé-valeur
   */
  public Map<String, Object> getUserInfo(String userId) {
    // Récupération du token, exception levée s'il est introuvable
    String token = getToken().orElseThrow(() -> new IllegalStateException("No token found"));

    // Définition du type de réponse attendu (un Map<String, Object>)
    var typeRef = new ParameterizedTypeReference<Map<String, Object>>() {};

    // Envoi de la requête GET pour récupérer les infos utilisateur
    ResponseEntity<Map<String, Object>> authorization =
      restClient.get()
        .uri(apiUrl + "/api/v1/user?id={id}", userId) // Endpoint pour récupérer un utilisateur avec son ID
        .header("Authorization", "Bearer " + token) // Ajout du token dans l'en-tête Authorization
        .accept(MediaType.APPLICATION_JSON) // Spécifie qu'on attend du JSON
        .retrieve()
        .toEntity(typeRef); // Récupération de la réponse sous forme de Map

    // Retourne le corps de la réponse qui contient les infos de l'utilisateur
    return authorization.getBody();
  }
}
