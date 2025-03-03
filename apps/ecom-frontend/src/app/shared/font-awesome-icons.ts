// Importation des modules nécessaires depuis Angular et d'autres bibliothèques

import { isPlatformBrowser, NgClass } from '@angular/common'; // `isPlatformBrowser` permet de vérifier si le code s'exécute sur le navigateur
import { Component, OnInit, inject, PLATFORM_ID } from '@angular/core'; // `PLATFORM_ID` permet de savoir si on est sur le serveur ou le client
import { RouterModule } from '@angular/router'; // Permet la gestion des routes dans Angular

// Importation des modules liés aux icônes FontAwesome
import { FaConfig, FaIconComponent, FaIconLibrary, IconDefinition } from '@fortawesome/angular-fontawesome';
import { faFacebook, faTwitter, faYoutube } from "@fortawesome/free-brands-svg-icons"; // Icônes des réseaux sociaux
import { faCartShopping, faTruckFast, faUser } from "@fortawesome/free-solid-svg-icons"; // Icônes liées à l'e-commerce

// Importation des composants de mise en page
import { FooterComponent } from '../layout/footer/footer.component'; // Pied de page de l'application
import { NavbarComponent } from '../layout/navbar/navbar.component'; // Barre de navigation

// Importation du service d'authentification OAuth2
import { Oauth2Service } from '../auth/oauth2.service';

// Définition des icônes utilisées dans l'application
export const fontAwesomeIcons: IconDefinition[] = [
    faUser, faCartShopping, // Icônes utilisateur et panier d'achat
    faTruckFast,            // Icône de livraison rapide
    faYoutube,
    faFacebook,
    faTwitter               // Icônes des réseaux sociaux
];

@Component({
  imports: [RouterModule, FaIconComponent, NavbarComponent, FooterComponent, NgClass], // Composants et modules nécessaires
  standalone: true, // Permet d'utiliser ce composant sans être dans un module spécifique
  selector: 'ecom-root', // Nom du composant utilisé dans le HTML
  templateUrl: './app.component.html', // Chemin du fichier HTML associé à ce composant
  styleUrl: './app.component.scss', // Chemin du fichier de styles SCSS associé
})
export class AppComponent implements OnInit { // `OnInit` permet d'exécuter du code après l'initialisation du composant
  
  title = 'ecom-frontend'; // Titre de l'application

  // Injection des services et bibliothèques avec la nouvelle syntaxe Angular 15+
  private faConfig = inject(FaConfig); // Configuration de FontAwesome
  private faIconLibrary = inject(FaIconLibrary); // Bibliothèque d'icônes FontAwesome
  private oauth2Service = inject(Oauth2Service); // Service d'authentification OAuth2
  platformId = inject(PLATFORM_ID); // Permet d'identifier si on est côté serveur ou navigateur

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

  private initFontAwesome() {
    this.faConfig.defaultPrefix = 'far'; // Définit le préfixe par défaut des icônes à "far" (FontAwesome Regular)
    this.faIconLibrary.addIcons(...fontAwesomeIcons); // Ajoute les icônes définies précédemment
  }
}
