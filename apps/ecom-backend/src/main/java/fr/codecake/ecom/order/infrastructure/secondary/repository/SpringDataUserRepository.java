import fr.codecake.ecom.order.domain.user.aggregate.User;
import fr.codecake.ecom.order.domain.user.repository.UserRepository;
import fr.codecake.ecom.order.domain.user.vo.UserAddressToUpdate;
import fr.codecake.ecom.order.domain.user.vo.UserEmail;
import fr.codecake.ecom.order.domain.user.vo.UserPublicId;
import fr.codecake.ecom.order.infrastructure.secondary.entity.UserEntity;
import fr.codecake.ecom.order.infrastructure.secondary.repository.JpaUserRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

// Indique que cette classe est un Repository Spring (permet l'injection de dépendance et la gestion par Spring)
@Repository
public class SpringDataUserRepository implements UserRepository {

  // Dépendance vers JpaUserRepository (interface de Spring Data JPA)
  private final JpaUserRepository jpaUserRepository;

  // Injection du repository via le constructeur
  public SpringDataUserRepository(JpaUserRepository jpaUserRepository) {
    this.jpaUserRepository = jpaUserRepository;
  }

  /**
   * Sauvegarde ou met à jour un utilisateur dans la base de données.
   * - Si l'utilisateur a déjà un `dbId`, il est mis à jour.
   * - Sinon, il est enregistré comme une nouvelle entrée.
   */
  @Override
  public void save(User user) {
    if(user.getDbId() != null) { // Vérifie si l'utilisateur a déjà un ID en base
      Optional<UserEntity> userToUpdateOpt = jpaUserRepository.findById(user.getDbId());
      if(userToUpdateOpt.isPresent()) {
        UserEntity userToUpdate = userToUpdateOpt.get();
        userToUpdate.updateFromUser(user); // Mise à jour de l'entité existante
        jpaUserRepository.save(UserEntity.from(user)); // Enregistrement après modification
      } else {
        jpaUserRepository.save(UserEntity.from(user)); // Création d'un nouvel utilisateur si non trouvé
      }
    }
  }

  /**
   * Récupère un utilisateur en fonction de son identifiant public (UUID).
   * - Retourne un `Optional<User>` pour gérer le cas où l'utilisateur n'existe pas.
   */
  @Override
  public Optional<User> get(UserPublicId userPublicId) {
    return jpaUserRepository.findOneByPublicId(userPublicId.value()) // Recherche par UUID
      .map(UserEntity::toDomain); // Conversion de UserEntity vers User (objet métier)
  }

  /**
   * Récupère un utilisateur à partir de son adresse e-mail.
   * - Retourne un `Optional<User>` pour éviter les `null`.
   */
  @Override
  public Optional<User> getOneByEmail(UserEmail userEmail) {
    return jpaUserRepository.findByEmail(userEmail.value()) // Recherche par email
      .map(UserEntity::toDomain); // Conversion en objet métier
  }

  /**
   * Met à jour l'adresse d'un utilisateur dans la base de données.
   * - Utilise une requête SQL optimisée sans charger l'entité complète.
   */
  @Override
  public void updateAddress(UserPublicId userPublicId, UserAddressToUpdate userAddress) {
    jpaUserRepository.updateAddress(
      userPublicId.value(), // UUID de l'utilisateur
      userAddress.userAddress().street(),
      userAddress.userAddress().city(),
      userAddress.userAddress().country(),
      userAddress.userAddress().zipCode()
    );
  }
}
