//import { IconDefinition } from "@fortawesome/free-brands-svg-icons";
import { NgClass } from '@angular/common';
import { Component, OnInit, inject, PLATFORM_ID } from '@angular/core';
import { RouterModule } from '@angular/router';
import { FaConfig, FaIconComponent, FaIconLibrary, IconDefinition } from '@fortawesome/angular-fontawesome';
import { faFacebook, faTwitter, faYoutube } from "@fortawesome/free-brands-svg-icons";
import { faCartShopping, faTruckFast, faUser } from "@fortawesome/free-solid-svg-icons";
import { FooterComponent } from '../layout/footer/footer.component';
import { NavbarComponent } from '../layout/navbar/navbar.component';

export const fontAwesomeIcons: IconDefinition [] = [
    faUser, faCartShopping,
    faTruckFast,
    faYoutube,
    faFacebook,
    faTwitter];
@Component({
  imports: [RouterModule, FaIconComponent, NavbarComponent, FooterComponent, NgClass],
  standalone: true,
  selector: 'ecom-root',
  templateUrl: './app.component.html',
  styleUrl: './app.component.scss',
})
export class AppComponent implements OnInit {
  title = 'ecom-frontend';
  private faConfig = inject(FaConfig);

  private faIconLibrary = inject(FaIconLibrary);

  private oauth2Service = inject(Oauth);

  platformId = inject(PLATFORM_ID);

  ngOnInit(): void {
    this.initFontAwesome();
  }

  private initFontAwesome() {
    this.faConfig.defaultPrefix = 'far';
    this.faIconLibrary.addIcons(...fontAwesomeIcons);
  }
}
