// Déclaration du package auquel appartient cette classe.
package fr.codecake.ecom.shared.error.domain;

import java.util.Map;

/**
 * Classe abstraite représentant une exception d'assertion.
 * Elle hérite de RuntimeException et est utilisée pour signaler des erreurs de validation.
 */
public abstract class AssertionException extends RuntimeException {

  // Champ représentant le nom du champ qui a causé l'erreur.
  private final String field;

  /**
   * Constructeur protégé pour empêcher l'instanciation directe en dehors des sous-classes.
   * @param field Nom du champ ayant déclenché l'erreur.
   * @param message Message décrivant l'erreur.
   */
  protected AssertionException(String field, String message) {
    super(message); // Appelle le constructeur de RuntimeException avec le message d'erreur.
    this.field = field;
  }

  /**
   * Méthode abstraite devant être implémentée par les sous-classes.
   * Elle retourne le type d'erreur associé à l'exception.
   * @return Le type d'erreur sous forme d'AssertionErrorType.
   */
  public abstract AssertionErrorType type();

  /**
   * Retourne le nom du champ ayant provoqué l'exception.
   * @return Le champ sous forme de chaîne de caractères.
   */
  public String field() {
    return field;
  }

  /**
   * Méthode pouvant être redéfinie pour fournir des paramètres supplémentaires liés à l'erreur.
   * Par défaut, elle retourne une Map vide.
   * @return Une Map contenant les paramètres de l'erreur.
   */
  public Map<String, String> parameters() {
    return Map.of(); // Retourne une Map immuable vide.
  }
}
