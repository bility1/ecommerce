import { bootstrapApplication } from '@angular/platform-browser';
import { appConfig } from './app/app.config';
import { AppComponent } from './app/shared/font-awesome-icons';

bootstrapApplication(AppComponent, appConfig).catch((err) =>
  console.error(err)
);
