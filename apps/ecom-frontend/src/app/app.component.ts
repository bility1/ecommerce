// Importation des modules nécessaires depuis Angular
import { Component, inject, OnInit, PLATFORM_ID } from '@angular/core'; 
// `Component` permet de définir un composant Angular
// `inject` est utilisé pour la nouvelle approche d'injection de dépendances dans Angular 15+
// `OnInit` est une interface qui permet d'exécuter du code après l'initialisation du composant
// `PLATFORM_ID` est utilisé pour identifier si le code s'exécute côté navigateur ou serveur (SSR)

import { RouterModule } from '@angular/router'; 
// `RouterModule` permet la gestion des routes dans l'application Angular

// Importation des modules liés aux icônes FontAwesome
import { 
  FaConfig,       // Configuration des icônes FontAwesome
  FaIconComponent, // Composant Angular pour afficher une icône FontAwesome
  FaIconLibrary,   // Bibliothèque qui stocke les icônes FontAwesome importées
} from '@fortawesome/angular-fontawesome';

import { fontAwesomeIcons } from './shared/font-awesome-icons'; 
// Fichier qui contient la liste des icônes utilisées dans l'application

// Importation des composants de mise en page
import { NavbarComponent } from './layout/navbar/navbar.component'; 
import { FooterComponent } from './layout/footer/footer.component';

// Importation des fonctions et modules Angular utiles
import { isPlatformBrowser, NgClass } from '@angular/common'; 
import { Oauth2Service } from './auth/oauth2.service';
// `isPlatformBrowser` permet de vérifier si le code s'exécute sur le navigateur
// `NgClass` est une directive Angular qui permet d'ajouter des classes CSS dynamiquement



// Définition du composant principal de l'application
@Component({
  standalone: true, // Permet d'utiliser ce composant sans dépendre d'un module Angular spécifique
  imports: [
    RouterModule,      // Permet l'utilisation des routes
    FaIconComponent,   // Composant pour afficher les icônes FontAwesome
    NavbarComponent,   // Barre de navigation
    FooterComponent,   // Pied de page
    NgClass,           // Directive Angular pour la gestion des classes CSS dynamiques
  ],
  selector: 'ecom-root', // Nom du composant utilisé dans le HTML
  templateUrl: './app.component.html', // Chemin du fichier HTML associé au composant
  styleUrl: './app.component.scss', // Chemin du fichier SCSS associé au composant
})
export class AppComponent implements OnInit { // `OnInit` est utilisé pour exécuter du code après l'initialisation du composant
  
  private faIconLibrary = inject(FaIconLibrary); // Injection de la bibliothèque FontAwesome
  private faConfig = inject(FaConfig); // Injection de la configuration FontAwesome

  private oauth2Service = inject(Oauth2Service); // Injection du service d'authentification OAuth2

    platformId = inject(PLATFORM_ID); // Injection de l'ID de la plateforme (navigateur ou serveur)

  constructor() {
    // Vérifie si le code est exécuté sur le navigateur avant d'initialiser l'authentification
    if (isPlatformBrowser(this.platformId)) {
      this.oauth2Service.initAuthentication(); // Vérifie si l'utilisateur est authentifié
    }
    // Récupère l'utilisateur connecté via un appel au service OAuth2
    this.oauth2Service.connectedUserQuery = this.oauth2Service.fetch();
  }

  ngOnInit(): void {
    this.initFontAwesome(); // Initialise les icônes FontAwesome au démarrage du composant
  }
  //

  private initFontAwesome() {
    this.faConfig.defaultPrefix = 'far'; // Définit `far` (FontAwesome Regular) comme préfixe par défaut pour les icônes
    this.faIconLibrary.addIcons(...fontAwesomeIcons); // Ajoute toutes les icônes définies dans `fontAwesomeIcons`
  }
}
