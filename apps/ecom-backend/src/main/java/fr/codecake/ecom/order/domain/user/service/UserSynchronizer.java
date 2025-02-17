package fr.codecake.ecom.order.domain.user.service;

// Importation des classes nécessaires
import fr.codecake.ecom.order.domain.user.aggregate.User;
import fr.codecake.ecom.order.domain.user.repository.UserRepository;
import fr.codecake.ecom.order.domain.user.vo.UserAddressToUpdate;
import fr.codecake.ecom.order.infrastructure.secondary.service.kinde.KindeService;
import fr.codecake.ecom.shared.authentification.application.AuthenticatedUser;
import org.springframework.security.oauth2.jwt.Jwt;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Service de synchronisation des utilisateurs entre l'application et le fournisseur d'identité (Kinde).
 */
public class UserSynchronizer {

  // Dépendances nécessaires pour la synchronisation
  private final UserRepository userRepository; // Interface pour interagir avec la base de données
  private final KindeService kindeService; // Service pour récupérer les infos utilisateur depuis Kinde

  // Clé utilisée pour récupérer la date de dernière connexion d'un utilisateur dans le token JWT
  private static final String UPDATE_AT_KEY = "last_signed_in";

  /**
   * Constructeur de UserSynchronizer qui prend en paramètre les dépendances nécessaires.
   *
   * @param userRepository Interface permettant d'interagir avec la base de données utilisateur
   * @param kindeService Service permettant de récupérer les informations des utilisateurs via Kinde
   */
  public UserSynchronizer(UserRepository userRepository, KindeService kindeService) {
    this.userRepository = userRepository;
    this.kindeService = kindeService;
  }

  /**
   * Synchronise les informations d'un utilisateur avec les données de l'Identity Provider (Kinde).
   *
   * @param jwtToken Token JWT contenant les informations de l'utilisateur
   * @param forceResync Indique si la synchronisation doit être forcée, même si aucune modification n'est détectée
   */
  public void syncWithIdp(Jwt jwtToken, boolean forceResync){
    // Extraction des informations du token JWT
    Map<String , Object> claims = jwtToken.getClaims();

    // Extraction des rôles de l'utilisateur à partir du token JWT
    List<String> rolesFromToken = AuthenticatedUser.extractRolesFromToken(jwtToken);

    // Récupération des informations de l'utilisateur depuis Kinde
    Map<String, Object> userInfo = kindeService.getUserInfo(claims.get("sub").toString());

    // Création d'un objet User avec les informations récupérées
    User user = User.fromTokenAttributes(userInfo, rolesFromToken);

    // Vérification si l'utilisateur existe déjà en base de données
    Optional<User> existingUser = userRepository.getOneByEmail(user.getEmail());

    if(existingUser.isPresent()){
      // Vérification de la date de dernière modification dans le token JWT
      if(claims.get(UPDATE_AT_KEY) != null){
        // Récupération des dates de dernière modification
        Instant lastModifiedDate = existingUser.orElseThrow().getLastModifiedDate();
        Instant idpModifiedDate = Instant.ofEpochSecond((Integer) claims.get(UPDATE_AT_KEY));

        // Mise à jour de l'utilisateur si les données ont changé ou si la synchronisation est forcée
        if(idpModifiedDate.isAfter(lastModifiedDate) || forceResync) {
          updateUser(user, existingUser.get());
        }
      }
    } else {
      // Si l'utilisateur n'existe pas en base, initialisation de ses champs et enregistrement
      user.initFieldForSignup();
      userRepository.save(user);
    }
  }

  /**
   * Met à jour les informations d'un utilisateur existant dans la base de données.
   *
   * @param user Données mises à jour de l'utilisateur
   * @param existingUser Utilisateur existant en base de données
   */
  private void updateUser(User user, User existingUser) {
    // Mise à jour des champs de l'utilisateur existant avec les nouvelles informations
    existingUser.updateFromUser(user);

    // Sauvegarde de l'utilisateur mis à jour en base de données
    userRepository.save(existingUser);
  }

  /**
   * Met à jour l'adresse d'un utilisateur en base de données.
   *
   * @param userAddressToUpdate Objet contenant la nouvelle adresse et l'identifiant de l'utilisateur
   */
  public void updateAddress(UserAddressToUpdate userAddressToUpdate){
    // Mise à jour de l'adresse dans la base de données
    userRepository.updateAddress(userAddressToUpdate.userPublicId(), userAddressToUpdate);
  }
}
