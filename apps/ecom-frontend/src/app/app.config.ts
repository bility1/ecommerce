import { ApplicationConfig } from '@angular/core';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { appRoutes } from './app.routes';

import { provideHttpClient, withFetch, withInterceptors } from '@angular/common/http';
import { authInterceptor, provideAuth, LogLevel, AbstractSecurityStorage } from 'angular-auth-oidc-client';
import { environment } from '../environments/environment.development';
import { SsrStorageService } from './auth/ssr-storage.service';
import { provideQueryClient, QueryClient} from '@tanstack/angular-query-experimental';

export const appConfig: ApplicationConfig = {
  providers: [
    
    provideRouter(appRoutes, withComponentInputBinding()),
    provideHttpClient(withFetch(), withInterceptors([authInterceptor()])),
    provideAuth({
      config:{
        authority : environment.kinde.authority,
        redirectUrl: environment.kinde.redirectUrl,
        postLogoutRedirectUri: environment.kinde.postLogoutRedirectUri,
        clientId: environment.kinde.cliendId,
        scope: "openid profile email offline",
        responseType:'code',
        silentRenew: true,
        useRefreshToken: true,
        logLevel: LogLevel.Warn,
        secureRoutes: [environment.apiUrl],
        customParamsAuthRequest:{
          audience: environment.kinde.audience,
          },
        },
      }),
      {provide: AbstractSecurityStorage, useClass: SsrStorageService},
      provideQueryClient(new QueryClient)
  ],
};
