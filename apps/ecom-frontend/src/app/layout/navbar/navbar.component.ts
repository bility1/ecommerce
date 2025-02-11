import { CommonModule } from "@angular/common"; // Importe les fonctionnalités Angular communes (ex: directives ngIf, ngFor)
import { Component } from "@angular/core"; // Importe le décorateur @Component pour déclarer un composant Angular
import { RouterLink } from "@angular/router"; // Permet d'utiliser les liens de navigation Angular
import { FaIconComponent } from "@fortawesome/angular-fontawesome"; // Importe le composant FontAwesome pour afficher des icônes

// Déclaration du composant Angular
@Component({

    selector: 'ecom-navbar', // Définit le sélecteur utilisé pour insérer ce composant dans un template
    standalone: true, // Indique que ce composant est autonome et ne dépend pas d’un module Angular
    imports: [CommonModule, RouterLink, FaIconComponent], // Liste des modules et composants importés pour fonctionner dans ce composant
    templateUrl: './navbar.component.html', // Chemin vers le fichier HTML associé à ce composant
    styleUrl:'./navbar.component.scss', // Chemin vers le fichier de styles SCSS pour ce composant
})
export class NavbarComponent{}
