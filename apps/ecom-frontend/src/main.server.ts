import { bootstrapApplication } from '@angular/platform-browser';
import { AppComponent } from './app/shared/font-awesome-icons';
import { config } from './app/app.config.server';

const bootstrap = () => bootstrapApplication(AppComponent, config);

export default bootstrap;
