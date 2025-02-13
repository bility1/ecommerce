package fr.codecake.ecom.wire.security.primary;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.core.Ordered;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@Configuration // 🔹 Indique que cette classe est une configuration Spring (elle sera détectée automatiquement par Spring Boot).
public class CorsFilterConfiguration {

  private final CorsConfiguration corsConfiguration; // 🔹 Stocke la configuration CORS injectée par Spring.

  public CorsFilterConfiguration(CorsConfiguration corsConfiguration) {
    this.corsConfiguration = corsConfiguration; // 🔹 Injection de la configuration CORS via le constructeur.
  }

  @Bean // 🔹 Définit un bean Spring qui sera géré par le conteneur.
  public FilterRegistrationBean<CorsFilter> simpleCorsFilter() {
    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();

    // 🔹 Applique la configuration CORS à tous les chemins de l'application (/**).
    source.registerCorsConfiguration("/**", corsConfiguration);

    // 🔹 Crée un filtre CORS avec la configuration définie.
    FilterRegistrationBean<CorsFilter> bean = new FilterRegistrationBean<>(new CorsFilter(source));

    // 🔹 Définit l'ordre d'exécution du filtre pour qu'il soit appliqué en priorité.
    bean.setOrder(Ordered.HIGHEST_PRECEDENCE);

    return bean;
  }
}
