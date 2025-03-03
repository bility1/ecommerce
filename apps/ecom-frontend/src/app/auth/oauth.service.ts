// Importation des modules nécessaires
import { HttpClient, HttpParams } from '@angular/common/http'; // Permet de faire des requêtes HTTP
import { inject, Injectable } from '@angular/core'; // Injectable permet d'injecter ce service dans d'autres classes
import { CreateQueryResult, injectQuery } from '@tanstack/angular-query-experimental'; // Gestion de cache et requêtes via TanStack Query
import { OidcSecurityService } from 'angular-auth-oidc-client'; // Service pour gérer l'authentification OIDC (OpenID Connect)
import { ConnectedUser } from '../shared/model/user.model'; // Modèle représentant un utilisateur authentifié
import { firstValueFrom, Observable } from 'rxjs'; // Gestion des flux de données asynchrones avec RxJS
import { environment } from '../../environments/environment';

// Décorateur Injectable pour rendre le service disponible dans toute l'application
@Injectable({
  providedIn: 'root' // Indique que ce service est accessible partout dans l'application sans avoir besoin de l'importer dans un module
})
export class Oauth2Service {

  // Injection des services avec la nouvelle API Angular 15+
  http = inject(HttpClient); // Service HTTP pour envoyer des requêtes API
  oidcSecurityService = inject(OidcSecurityService); // Service de gestion de l'authentification OIDC

  // Stocke la requête pour récupérer l'utilisateur connecté avec TanStack Query
  connectedUserQuery: CreateQueryResult<ConnectedUser> | undefined ;

  // Message indiquant qu'aucun utilisateur n'est connecté
  notConnected = 'NOT_CONNECTED';

  /**
   * Récupère l'utilisateur connecté en utilisant TanStack Query
   * @returns CreateQueryResult<ConnectedUser>
   */
  fetch(): CreateQueryResult<ConnectedUser> {
    return injectQuery(() => ({
        queryKey: ['connected-user'], // Clé unique pour identifier cette requête en cache
        queryFn: () => firstValueFrom(this.fetchUserHttp(false)), // Exécute la requête HTTP et transforme l'Observable en Promise
    }));
  }

  /**
   * Effectue une requête HTTP pour récupérer l'utilisateur connecté
   * @param forceResync - Booléen indiquant si les données doivent être rafraîchies
   * @returns Observable<ConnectedUser>
   */
  fetchUserHttp(forceResync: boolean): Observable<ConnectedUser> {
    const params = new HttpParams().set('forceResync', forceResync); // Ajoute un paramètre forceResync à l'URL
    return this.http.get<ConnectedUser>(`${environment.apiUrl}/users/authenticated`, { params }); // Effectue la requête GET
  }

  /**
   * Déclenche la connexion en redirigeant l'utilisateur vers le fournisseur OAuth
   */
  login(): void {
    this.oidcSecurityService.authorize(); // Lance le processus de connexion OAuth
  }

  /**
   * Déconnecte l'utilisateur et termine la session OAuth
   */
  logout(): void {
    this.oidcSecurityService.logoff().subscribe(); // Effectue la déconnexion et nettoie la session utilisateur
  }

  /**
   * Vérifie si l'utilisateur est authentifié et affiche un message en console
   */
  initAuthentication(): void {
    this.oidcSecurityService.checkAuth().subscribe((authInfo) => { // Vérifie l'état de l'authentification
      if (authInfo.isAuthenticated) {
        console.log('connected'); // L'utilisateur est connecté
      } else {
        console.log('not connected'); // L'utilisateur n'est pas connecté
      }
    });
  }

  /**
   * Vérifie si l'utilisateur possède une ou plusieurs autorisations spécifiques
   * @param ConnectedUser - L'utilisateur connecté contenant ses rôles
   * @param authorities - Liste des rôles à vérifier (peut être un tableau ou une seule chaîne)
   * @returns boolean - True si l'utilisateur possède au moins une des autorisations, sinon False
   */
  hasAnyAuthorities(ConnectedUser: ConnectedUser, authorities: Array<string> | string): boolean {
    if (!Array.isArray(authorities)) {
      authorities = [authorities]; // Convertit en tableau si ce n'est pas déjà un tableau
    }
    
    if (ConnectedUser.authorities) { // Vérifie si l'utilisateur a des rôles
      return ConnectedUser.authorities.some((authority: string) => authorities.includes(authority)); // Vérifie si au moins un rôle correspond
    } else {
      return false; // L'utilisateur n'a pas d'autorisation
    }
  }
}
